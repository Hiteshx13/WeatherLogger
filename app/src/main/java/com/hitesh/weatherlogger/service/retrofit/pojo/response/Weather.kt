package com.hitesh.weatherlogger.service.retrofit.pojo.response

import android.os.Parcel
import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Weather(


    @PrimaryKey(autoGenerate = true)
    var PKWeather: Int = 0,

    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("main")
    @Expose
    var main: String? = null,
    @SerializedName("description")
    @Expose
    var description: String? = null,
    @SerializedName("icon")
    @Expose
    var icon: String = ""

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(PKWeather)
        parcel.writeValue(id)
        parcel.writeString(main)
        parcel.writeString(description)
        parcel.writeString(icon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Weather> {
        override fun createFromParcel(parcel: Parcel): Weather {
            return Weather(
                parcel
            )
        }

        override fun newArray(size: Int): Array<Weather?> {
            return arrayOfNulls(size)
        }
    }
}
