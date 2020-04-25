package com.accenture.weatherlogger.service.retrofit.pojo.response

import android.os.Parcel
import android.os.Parcelable

data class WeatherDetail(
    var IDWeather: Int = 0,
    var lat: Double? = null,
    var lon: Double? = null,
    var timezone: String? = null,
    var current: Current? = null,
    var hourly: List<Hourly?>? = null,
    var daily: List<Daily?>? = null,
    var isSuccess: Boolean = false,
    var apiResponseMessage: String? = ""
) : Parcelable {
    fun getCity(): String {
        return timezone?.substring((timezone?.indexOf("/")?:0 + 1), timezone?.length?:0).toString()
    }

    fun getCityAndRegion(): String {
        val city = timezone?.substring(timezone?.indexOf("/")?:0 + 1, timezone?.length?:0)
        val cityReg = timezone?.substring(0, timezone?.indexOf("/")?:0)
        return "$city \n ( $cityReg )"
    }

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readString(),
        source.readParcelable<Current>(
            Current::class.java.classLoader
        ),
        source.createTypedArrayList(Hourly.CREATOR),
        source.createTypedArrayList(Daily),
        1 == source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(IDWeather)
        writeValue(lat)
        writeValue(lon)
        writeString(timezone)
        writeParcelable(current, 0)
        writeTypedList(hourly)
        writeTypedList(daily)
        writeInt((if (isSuccess) 1 else 0))
        writeString(apiResponseMessage)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<WeatherDetail> =
            object : Parcelable.Creator<WeatherDetail> {
                override fun createFromParcel(source: Parcel): WeatherDetail =
                    WeatherDetail(
                        source
                    )

                override fun newArray(size: Int): Array<WeatherDetail?> = arrayOfNulls(size)
            }
    }
}