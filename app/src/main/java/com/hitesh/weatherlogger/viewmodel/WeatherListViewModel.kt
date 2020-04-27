package com.hitesh.weatherlogger.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hitesh.weatherlogger.service.repository.WeatherListRepository
import com.hitesh.weatherlogger.service.retrofit.pojo.request.ReqWeather
import com.hitesh.weatherlogger.service.retrofit.pojo.response.WeatherDetail

class WeatherListViewModel(application: Application) :
    AndroidViewModel(application) {
    var mutableResponse = MutableLiveData<WeatherDetail>()
    var weatherListRepository =
        WeatherListRepository()

    fun getWeatherDetails(reqWeather: ReqWeather) {
        weatherListRepository.getWeatherDetails(reqWeather, mutableResponse)
    }

    fun getWeatherData(): MutableLiveData<WeatherDetail> {
        return mutableResponse
    }
}