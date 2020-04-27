package com.accenture.weatherlogger.view.activity

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.accenture.weatherlogger.R
import com.accenture.weatherlogger.service.database.AppDatabase
import com.accenture.weatherlogger.service.retrofit.pojo.response.WeatherDetail
import com.accenture.weatherlogger.service.utils.WIDGET_KEY
import com.google.gson.Gson

/**
 * Implementation of App Widget functionality.
 */
class WeatherWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

    var pref = context.getSharedPreferences(context.resources.getString(R.string.app_name), 0)
    var editText = pref.edit()
    editText.putInt(WIDGET_KEY, appWidgetId)
    val roomDB: AppDatabase = AppDatabase.getDatabase(context)
    val totalDBRecords = roomDB.getWeatherDao().getCount()

    val remoteViews: RemoteViews
    if (totalDBRecords == 0) {
        remoteViews = RemoteViews(context.packageName, R.layout.weather_widget_day)
        remoteViews.setTextViewText(
            R.id.tvStatus,
            context.resources.getString(R.string.no_data_found)
        )
    } else {
        val model = roomDB.getWeatherDao().getAllRecords()[totalDBRecords - 1]
        val modelTemprature: WeatherDetail =
            Gson().fromJson(model.WeatherData, WeatherDetail::class.java)

        remoteViews = if (totalDBRecords % 2 == 0) {
            RemoteViews(context.packageName, R.layout.weather_widget_day)
        } else {
            RemoteViews(context.packageName, R.layout.weather_widget_night)
        }
        remoteViews.setTextViewText(R.id.tvTemprature, modelTemprature.current?.getTemprature())
        remoteViews.setTextViewText(R.id.tvStatus, modelTemprature.current?.getStatus())
    }
    remoteViews.setOnClickPendingIntent(R.id.llRootWidget, pendingIntent)
    appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
}