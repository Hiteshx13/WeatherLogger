package com.hitesh.weatherlogger.service.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class WeatherDetailString(
    @PrimaryKey(autoGenerate = true)
    var IDWeather: Int = 0,
    var WeatherData: String,
    var deviceTime: String,
    var isAnimation: Boolean = false
)