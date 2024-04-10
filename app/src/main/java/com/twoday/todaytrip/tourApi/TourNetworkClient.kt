package com.twoday.todaytrip.tourApi

import android.util.Log
import com.google.common.net.HttpHeaders.CACHE_CONTROL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.utils.NetworkUtil
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


object TourNetworkClient {
    private const val TOUR_BASE_URL = "https://apis.data.go.kr/B551011/KorService1/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val tourRetrofit = Retrofit.Builder()
        .baseUrl(TOUR_BASE_URL)
        .addConverterFactory(
            MoshiConverterFactory.create(moshi)
        )
        .client(createOkHttpClient())
        .build()

    val tourNetWork: TourNetworkInterface =
        tourRetrofit.create(TourNetworkInterface::class.java)

    private fun createOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(provideOfflineCacheInterceptor())
            .addNetworkInterceptor(provideCacheInterceptor())
            .addNetworkInterceptor(loggingInterceptor)
            .cache(provideCache())
            .build()
    }

    private fun provideCache(): Cache? {
        var cache: Cache? = null
        try {
            cache = Cache(
                File(MyApplication.appContext!!.cacheDir, "http-cache"),
                10 * 1024 * 1024
            ) // 10 MB
        } catch (e: Exception) {
            Log.d("TourNetworkClient", "Could not create Cache!")
        }
        return cache
    }

    private fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response: Response = chain.proceed(chain.request())

            // re-write response header to force use of cache
            val cacheControl: CacheControl = CacheControl.Builder()
                .maxAge(2, TimeUnit.MINUTES)
                .build()
            response.newBuilder()
                .header(CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }

    private fun provideOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!NetworkUtil.isNetworkAvailable()) {
                val cacheControl: CacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
    }
}
