package com.accenture.weatherlogger.service.retrofit.pojo.response

import android.os.Parcel
import android.os.Parcelable
import androidx.room.PrimaryKey
import com.accenture.weatherlogger.service.utils.convertTemprature
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Temp(
    @PrimaryKey
    var IDTemp: Int = 0,
    @SerializedName("day")
    @Expose
    var day: Double? = null,
    @SerializedName("min")
    @Expose
    var min: Double? = null,
    @SerializedName("max")
    @Expose
    var max: Double? = null,
    @SerializedName("night")
    @Expose
    var night: Double? = null,
    @SerializedName("eve")
    @Expose
    var eve: Double? = null,
    @SerializedName("morn")
    @Expose
    var morn: Double? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readInt(),
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readValue(Double::class.java.classLoader) as Double?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(IDTemp)
        writeValue(day)
        writeValue(min)
        writeValue(max)
        writeValue(night)
        writeValue(eve)
        writeValue(morn)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Temp> = object : Parcelable.Creator<Temp> {
            override fun createFromParcel(source: Parcel): Temp =
                Temp(
                    source
                )

            override fun newArray(size: Int): Array<Temp?> = arrayOfNulls(size)
        }
    }


    fun getTempratureDay(): String {
        return "Day : ${convertTemprature(
            day
        )}"
    }

    fun getTempratureNight(): String {
        return "Night : ${convertTemprature(
            night
        )}"
    }
}
