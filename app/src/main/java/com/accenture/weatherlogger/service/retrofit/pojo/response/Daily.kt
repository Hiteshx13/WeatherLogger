package com.accenture.weatherlogger.service.retrofit.pojo.response

import android.os.Parcel
import android.os.Parcelable
import com.accenture.weatherlogger.service.utils.convertUTCtoDateTime
import com.accenture.weatherlogger.service.utils.convertUTCtoTime
import com.accenture.weatherlogger.service.utils.getDayOfWeek


data class Daily(

    var IDDaily: Int = 0,
    var dt: Int? = null,
    var sunrise: Int? = null,
    var sunset: Int? = null,
    var temp: Temp? = null,
    var feelsLike: FeelsLike? = null,
    var pressure: Int? = null,
    var humidity: Int? = null,
    var dewPoint: Double? = null,
    var windSpeed: Double? = null,
    var windDeg: Int? = null,
    var weather: List<Weather?>? = null,
    var clouds: Int? = null,
    var uvi: Double? = null,
    var rain: Double? = null,
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
