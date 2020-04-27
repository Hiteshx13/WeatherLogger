package com.hitesh.weatherlogger.service.retrofit.pojo.response

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.hitesh.weatherlogger.service.utils.convertTemprature
import com.hitesh.weatherlogger.service.utils.convertUTCtoTime
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Hourly(
    @PrimaryKey
    @SerializedName("id_hourly")
    var IDHourly: Int = 0,
    @SerializedName("dt")
    @Expose
    var dt: Int? = null,
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
    @SerializedName("clouds")
    @Expose
    var clouds: Int? = null,
    @SerializedName("wind_speed")
    @Expose
    var windSpeed: Double? = null,
    @SerializedName("wind_deg")
    @Expose
    var windDeg: Int? = null,
    @Ignore
    @Expose
    @SerializedName("weather")
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
            var listWeather: List<Weather?>? = weather
            var weatherData: Weather? = listWeather?.get(0)
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
