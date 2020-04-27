package com.hitesh.weatherlogger.service.retrofit.pojo.response

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.hitesh.weatherlogger.service.utils.convertTemprature
import com.hitesh.weatherlogger.service.utils.convertUTCtoDateTime
import com.hitesh.weatherlogger.service.utils.convertUTCtoTime
import com.hitesh.weatherlogger.service.utils.getDayOfWeek
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Current(
    @PrimaryKey(autoGenerate = true)
    var IDCurrent: Int = 0,


    @SerializedName("dt")
    @Expose
    var dt: Int? = null,
    @SerializedName("sunrise")
    @Expose
    var sunrise: Int? = null,
    @SerializedName("sunset")
    @Expose
    var sunset: Int? = null,
    @SerializedName("temp")
    @Expose
    var temp: Double? = null,
    @SerializedName("feels_like")
    @Expose
    var feelsLike: Double? = null,
    @SerializedName("pressure")
    @Expose
    var pressure: Int? = null,
    @SerializedName("humidity")
    @Expose
    var humidity: Int? = null,
    @SerializedName("dew_point")
    @Expose
    var dewPoint: Double? = null,
    @SerializedName("uvi")
    @Expose
    var uvi: Double? = null,
    @SerializedName("clouds")
    @Expose
    var clouds: Int? = null,
    @SerializedName("visibility")
    @Expose
    var visibility: Int? = null,
    @SerializedName("wind_speed")
    @Expose
    var windSpeed: Double? = null,
    @SerializedName("wind_deg")
    @Expose
    var windDeg: Int? = null,

    @SerializedName("weather")
    @Ignore
    @Expose
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
            var listWeather: List<Weather?>? = weather
            var weatherData: Weather? = listWeather?.get(0)
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

