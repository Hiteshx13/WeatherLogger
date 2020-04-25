package com.accenture.weatherlogger.service.retrofit.pojo.response

import android.os.Parcel
import android.os.Parcelable
import com.accenture.weatherlogger.service.utils.convertTemprature
import com.accenture.weatherlogger.service.utils.convertUTCtoDateTime
import com.accenture.weatherlogger.service.utils.convertUTCtoTime
import com.accenture.weatherlogger.service.utils.getDayOfWeek


data class Current(

    var IDCurrent: Int = 0,

    var dt: Int? = null,
    var sunrise: Int? = null,
    var sunset: Int? = null,
    var temp: Double? = null,
    var feelsLike: Double? = null,
    var pressure: Int? = null,
    var humidity: Int? = null,
    var dewPoint: Double? = null,
    var uvi: Double? = null,
    var clouds: Int? = null,
    var visibility: Int? = null,
    var windSpeed: Double? = null,
    var windDeg: Int? = null,
    var weather: List<Weather?>? = null

) : Parcelable {
    fun getTemprature(): String {

        return if (temp == null) {
            " "
        } else {
            convertTemprature(temp)
        }
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

    fun getCurrentDate(): String {
        return convertUTCtoDateTime(
            dt ?: 0
        )
    }

    fun getCurrentDateTime(): String {
        return getDayOfWeek(
            dt ?: 0
        ) + " \n " + convertUTCtoDateTime(
            dt ?: 0
        )
    }

    private fun getSunrise(): String {
        return "Sunrise : " + convertUTCtoTime(
            sunrise ?: 0
        )
    }

    private fun getSunSet(): String {
        return "Sunset : " + convertUTCtoTime(
            sunset ?: 0
        )
    }

    fun getSunriseAndSunset(): String {
        return getSunrise() + "  " + getSunSet()
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

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.createTypedArrayList(Weather)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(IDCurrent)
        writeValue(dt)
        writeValue(sunrise)
        writeValue(sunset)
        writeValue(temp)
        writeValue(feelsLike)
        writeValue(pressure)
        writeValue(humidity)
        writeValue(dewPoint)
        writeValue(uvi)
        writeValue(clouds)
        writeValue(visibility)
        writeValue(windSpeed)
        writeValue(windDeg)
        writeTypedList(weather)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Current> = object : Parcelable.Creator<Current> {
            override fun createFromParcel(source: Parcel): Current =
                Current(
                    source
                )

            override fun newArray(size: Int): Array<Current?> = arrayOfNulls(size)
        }
    }
}

