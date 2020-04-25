package com.accenture.weatherlogger.service.retrofit.pojo.request

data class ReqWeather(
    var lat: String = "",
    var lon: String = "",
    var appid: String = ""
)