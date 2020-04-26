package com.accenture.weatherlogger.service.retrofit.pojo.response

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherDetail(
    @SerializedName("IDWeather")
    @PrimaryKey(autoGenerate = true)
    var IDWeather: Int = 0,

    @SerializedName("lat")
    @Expose
    var lat: Double? = null,
    @SerializedName("lon")
    @Expose
    var lon: Double? = null,
    @SerializedName("timezone")
    @Expose
    var timezone: String = "",
    @Ignore
    @SerializedName("current")
    @Expose
    var current: Current? = null,

    @SerializedName("hourly")
    @Expose
    var hourly: List<Hourly?>? = null,

    @SerializedName("daily")
    @Expose
    var daily: List<Daily?>? = null,
    var isSuccess: Boolean = false,
    var apiResponseMessage: String = ""
) : Parcelable {
    fun getCity(): String {
        return timezone.substring(timezone.indexOf("/") + 1, timezone.length)
    }

    fun getCityAndRegion(): String {
        val city = timezone.substring(timezone.indexOf("/") + 1, timezone.length)
        val cityReg = timezone.substring(0, timezone.indexOf("/"))
        return "$city \n ( $cityReg )"
    }

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readString(),
        source.readParcelable<Current>(Current::class.java.classLoader),
        source.createTypedArrayList(Hourly.CREATOR),
        source.createTypedArrayList(Daily.CREATOR),
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
                override fun createFromParcel(source: Parcel): WeatherDetail = WeatherDetail(source)
                override fun newArray(size: Int): Array<WeatherDetail?> = arrayOfNulls(size)
            }
    }
}