package com.accenture.weatherlogger.service.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.accenture.weatherlogger.R
import com.accenture.weatherlogger.service.database.table.WeatherDetailString
import com.accenture.weatherlogger.service.database.dao.WeatherDao


@Database(
    entities = [WeatherDetailString::class], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    companion object {

        open fun getDatabase(context: Context): AppDatabase {
            var database: AppDatabase? = null
            var db_name = context.getString(R.string.app_name).toString() + ".db"
            if (database == null) {
                synchronized(AppDatabase::class) {
                    if (database == null) {
                        database = Room.databaseBuilder(
                            context,
                            AppDatabase::class.java, db_name
                        ).allowMainThreadQueries().addCallback(object : RoomDatabase.Callback() {

                        }).build()
                    }
                }
            }
            return database!!
        }


    }

    abstract fun getWeatherDao(): WeatherDao
}