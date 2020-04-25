package com.accenture.weatherlogger.service.repository

import androidx.lifecycle.MutableLiveData
import com.accenture.weatherlogger.service.retrofit.ApiInterface
import com.accenture.weatherlogger.service.retrofit.RetrofitClient
import com.accenture.weatherlogger.service.retrofit.pojo.request.ReqWeather
import com.accenture.weatherlogger.service.retrofit.pojo.response.WeatherDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherListRepository {
    var apiInt: ApiInterface? = null

    init {
        if (apiInt == null) {
            apiInt = RetrofitClient.getRetrofitInstance().create(
                ApiInterface::class.java
            )
        }
    }

    fun getWeatherDetails(
        reqModel: ReqWeather,
        mutableResponse: MutableLiveData<WeatherDetail>
    ): MutableLiveData<WeatherDetail> {

        val call: Call<WeatherDetail?>? = apiInt?.getWeather(
            reqModel.lat,
            reqModel.lon,
            reqModel.appid
        )
        call!!.enqueue(object : Callback<WeatherDetail?> {

            override fun onResponse(
                call: Call<WeatherDetail?>?,
                result: Response<WeatherDetail?>?
            ) {
                val responseBody = result?.body()
                responseBody?.isSuccess = true
                mutableResponse.value = responseBody
            }

            override fun onFailure(call: Call<WeatherDetail?>, t: Throwable) {
                val responseBody =
                    WeatherDetail()
                responseBody.isSuccess = false
                responseBody.apiResponseMessage = t.message ?: ""
                mutableResponse.value = responseBody
            }
        })
        return mutableResponse
    }
}