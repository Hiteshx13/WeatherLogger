package com.accenture.weatherlogger.service.retrofit.pojo.response

import android.os.Parcel
import android.os.Parcelable
import com.accenture.weatherlogger.service.utils.convertTemprature
import com.accenture.weatherlogger.service.utils.convertUTCtoTime

data class Hourly(

    var IDHourly: Int = 0,
    var dt: Int? = null,
    var temp: Double? = null,
    var feelsLike: Double? = null,
    var pressure: Int? = null,
    var humidity: Int? = null,
    var dewPoint: Double? = null,
    var clouds: Int? = null,
    var windSpeed: Double? = null,
    var windDeg: Int? = null,
    var weather: List<Weather?>? = null,
    var isAnimation: Boolean = false
) : Parcelable {


    fun timeData(): String {
        return convertUTCtoTime(
            dt ?: 0
        )
    }

    fun getTemprature(): String {

        return if (temp == null) {
            " "
        } else {
            convertTemprature(temp)
        }
    }


    fun getClouds(): String {
        return "$clouds %"
    }

    fun getHumidity(): String {
        return "Humidity : $humidity %"
    }

    fun getWindSpeed(): String {
        return "Wind Speed : $windSpeed m/h"
    }

    fun getFeelsLike(): String {
        return "Feels :  ${convertTemprature(
            feelsLike
        )}"
    }

    fun getStatus(): String {

        var status = ""
        if (!weather.isNullOrEmpty()) {
            val listWeather: List<Weather?>? = weather
            val weatherData: Weather? = listWeather?.get(0)
            status = weatherData?.description ?: ""
        }
        return status
    }

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.createTypedArrayList(Weather)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(IDHourly)
        writeValue(dt)
        writeValue(temp)
        writeValue(feelsLike)
        writeValue(pressure)
        writeValue(humidity)
        writeValue(dewPoint)
        writeValue(clouds)
        writeValue(windSpeed)
        writeValue(windDeg)
        writeTypedList(weather)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Hourly> = object : Parcelable.Creator<Hourly> {
            override fun createFromParcel(source: Parcel): Hourly =
                Hourly(
                    source
                )

            override fun newArray(size: Int): Array<Hourly?> = arrayOfNulls(size)
        }
    }
}
