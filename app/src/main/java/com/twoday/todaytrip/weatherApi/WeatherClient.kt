package com.twoday.todaytrip.weatherApi

import com.google.gson.GsonBuilder
import com.twoday.todaytrip.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object WeatherClient {
    private const val WEATHER_BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"
    private val gson = GsonBuilder().setLenient().create()
    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG)
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            interceptor.level = HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .build()
    }

    private val weatherRetrofit = Retrofit.Builder()
        .baseUrl(WEATHER_BASE_URL).addConverterFactory(GsonConverterFactory.create())
        .client(createOkHttpClient())
        .build()

    val weatherNetWork: WeatherInterface = weatherRetrofit.create(WeatherInterface::class.java)
}