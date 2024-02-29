package com.twoday.todaytrip.weather

import com.twoday.todaytrip.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherInterface {
    // getVilageFcst : 동네 예보 조회
    // getUltraSrtFcst
    @GET("getVilageFcst?serviceKey=sGsBo9qDtZTsAcQo8nG1ByeQJxOkUMKXwZrq9%2FIPhMgXhlN%2B7fYiuzf7O4eIPJN69UYCNRDljT9Iji1KeZZBdQ%3D%3D")

    fun getWeather(@Query("dataType") dataType : String,
                   @Query("numOfRows") numOfRows : Int,
                   @Query("pageNo") pageNo : Int,
                   @Query("base_date") baseDate : String,
                   @Query("base_time") baseTime : String,
                   @Query("nx") nx : String,
                   @Query("ny") ny : String)
            : Call<weather>
}

