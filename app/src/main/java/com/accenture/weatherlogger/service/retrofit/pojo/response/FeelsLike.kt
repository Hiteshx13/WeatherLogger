package com.accenture.weatherlogger.service.retrofit.pojo.response

import android.os.Parcel
import android.os.Parcelable

data class FeelsLike(
    var IDFeelslike: Int = 0,
    var day: Double? = null,
    var night: Double? = null,
    var eve: Double? = null,
    var morn: Double? = null

) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(IDFeelslike)
        writeValue(day)
        writeValue(night)
        writeValue(eve)
        writeValue(morn)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FeelsLike> = object : Parcelable.Creator<FeelsLike> {
            override fun createFromParcel(source: Parcel): FeelsLike =
                FeelsLike(
                    source
                )

            override fun newArray(size: Int): Array<FeelsLike?> = arrayOfNulls(size)
        }
    }

    fun getFeelsDay(): String {
        return "Day : $day"
    }

    fun getFeelsNight(): String {
        return "Night : $night"
    }

}

