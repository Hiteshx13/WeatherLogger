package com.hitesh.weatherlogger.view.callback

import com.hitesh.weatherlogger.service.retrofit.pojo.response.WeatherDetail

interface PositionClickListener {
    fun onClick(pos: Int, model: WeatherDetail)
}