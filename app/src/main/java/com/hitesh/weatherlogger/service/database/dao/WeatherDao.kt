package com.hitesh.weatherlogger.service.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hitesh.weatherlogger.service.database.table.WeatherDetailString

@Dao
interface WeatherDao {

    @Query("SELECT * FROM WeatherDetailString")
    fun getAllRecords(): List<WeatherDetailString>

    @Query("SELECT * FROM WeatherDetailString where IDWeather = :recordPos")
    fun getRecordFromPosition(recordPos:Int): WeatherDetailString

    @Insert
    fun insertData(weather: WeatherDetailString)

    @Query("SELECT COUNT(IDWeather) FROM WeatherDetailString")
    fun getCount(): Int

    @Delete
    fun delete(model: WeatherDetailString)

    @Query("DELETE  FROM WeatherDetailString where IDWeather = :recordId")
    fun deleteByPosition(recordId: Int)
}