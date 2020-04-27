package com.hitesh.weatherlogger.service.retrofit

import com.hitesh.weatherlogger.service.retrofit.pojo.response.WeatherDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("/data/2.5/onecall?")
    fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String
    ): Call<WeatherDetail?>?

}