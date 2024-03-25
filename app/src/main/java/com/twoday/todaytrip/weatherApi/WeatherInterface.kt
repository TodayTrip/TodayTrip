package com.twoday.todaytrip.weatherApi

import com.twoday.todaytrip.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val WEATHER_API_KEY = BuildConfig.WEATHER_API_KEY
interface WeatherInterface {
    // getVilageFcst : 동네 예보 조회
    // getUltraSrtFcst
    @GET(WEATHER_API_KEY)

    fun getWeather(@Query("dataType") dataType : String,
                   @Query("numOfRows") numOfRows : Int,
                   @Query("pageNo") pageNo : Int,
                   @Query("base_date") baseDate : String,
                   @Query("base_time") baseTime : String,
                   @Query("nx") nx : String,
                   @Query("ny") ny : String)
            : Call<weather>
}

