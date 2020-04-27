package com.hitesh.weatherlogger.service.retrofit.pojo.response

import android.os.Parcel
import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class FeelsLike(
    @PrimaryKey
    @SerializedName("id_feelslike")
    var IDFeelslike: Int = 0,

    @SerializedName("day")

    var day: Double? = null,

    @SerializedName("night")
    var night: Double? = null,

    @SerializedName("eve")
    var eve: Double? = null,
    @SerializedName("morn")

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

