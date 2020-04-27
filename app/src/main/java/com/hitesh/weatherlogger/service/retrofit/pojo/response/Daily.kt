package com.hitesh.weatherlogger.service.retrofit.pojo.response

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.hitesh.weatherlogger.service.utils.convertUTCtoDateTime
import com.hitesh.weatherlogger.service.utils.convertUTCtoTime
import com.hitesh.weatherlogger.service.utils.getDayOfWeek
import com.google.gson.annotations.Expose


data class Daily(


    @PrimaryKey
    // @SerializedName("id_daily")
    var IDDaily: Int = 0,

    // @SerializedName("dt")
    @Expose
    var dt: Int? = null,
    // @SerializedName("sunrise")
    @Expose
    var sunrise: Int? = null,
    // @SerializedName("sunset")
    @Expose
    var sunset: Int? = null,

    // @SerializedName("temp")
    @Expose
    @Ignore
    var temp: Temp? = null,

    // @SerializedName("feels_like")
    @Ignore
    @Expose
    var feelsLike:FeelsLike? = null,

    // @SerializedName("pressure")
    @Expose
    var pressure: Int? = null,
    // @SerializedName("humidity")
    @Expose
    var humidity: Int? = null,
    // @SerializedName("dew_point")
    @Expose
    var dewPoint: Double? = null,
    // @SerializedName("wind_speed")
    @Expose
    var windSpeed: Double? = null,
    // @SerializedName("wind_deg")
    @Expose
    var windDeg: Int? = null,

    // @SerializedName("weather")
    @Ignore
    var weather: List<Weather?>? = null,

    // @SerializedName("clouds")
    @Expose
    var clouds: Int? = null,
    // @SerializedName("uvi")
    @Expose
    var uvi: Double? = null,
    // @SerializedName("rain")
    @Expose
    var rain: Double? = null,
    // @SerializedName("snow")
    @Expose
    var snow: Double? = null


) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readParcelable(Temp::class.java.classLoader),
        parcel.readParcelable(FeelsLike::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createTypedArrayList(Weather),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    )

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

    fun getDayOfWeek(): String {
        return getDayOfWeek(dt ?: 0)
    }

    fun getSunrise(): String {
        return "Sunrise : " + convertUTCtoTime(
            sunrise ?: 0
        )
    }

    fun getSunSet(): String {
        return "Sunset: " + convertUTCtoTime(
            sunset ?: 0
        )
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(IDDaily)
        parcel.writeValue(dt)
        parcel.writeValue(sunrise)
        parcel.writeValue(sunset)
        parcel.writeParcelable(temp, flags)
        parcel.writeParcelable(feelsLike, flags)
        parcel.writeValue(pressure)
        parcel.writeValue(humidity)
        parcel.writeValue(dewPoint)
        parcel.writeValue(windSpeed)
        parcel.writeValue(windDeg)
        parcel.writeTypedList(weather)
        parcel.writeValue(clouds)
        parcel.writeValue(uvi)
        parcel.writeValue(rain)
        parcel.writeValue(snow)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Daily> {
        override fun createFromParcel(parcel: Parcel): Daily {
            return Daily(
                parcel
            )
        }

        override fun newArray(size: Int): Array<Daily?> {
            return arrayOfNulls(size)
        }
    }
}
