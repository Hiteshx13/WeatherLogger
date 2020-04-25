package com.accenture.weatherlogger.view.callback

import com.accenture.weatherlogger.service.retrofit.pojo.response.WeatherDetail

interface PositionClickListener {
    fun onClick(pos: Int, model: WeatherDetail)
}