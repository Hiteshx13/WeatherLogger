package com.accenture.weatherlogger.temp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.accenture.weatherlogger.R
import com.accenture.weatherlogger.service.database.AppDatabase
import com.accenture.weatherlogger.service.retrofit.pojo.response.WeatherDetail
import com.accenture.weatherlogger.service.utils.convertUTCtoHour24
import com.accenture.weatherlogger.view.activity.MainActivity
import com.google.gson.Gson

/**
 * Implementation of App Widget functionality.
 */
class WeatherWidgetTemp : AppWidgetProvider() {
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
  //  var roomDB: AppDatabase = AppDatabase.getDatabase(context)
    var intent = Intent(context, MainActivity::class.java)
    var pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
    val widgetText = context.getString(R.string.appwidget_text)

    //var totalDBRecords = roomDB.getWeatherDao().getCount()


    var remoteViews = RemoteViews(context.packageName, R.layout.weather_widget_day)
    remoteViews.setOnClickPendingIntent(R.id.llRootWidget, pendingIntent)
    remoteViews.setTextViewText(R.id.tvTemprature, "-")
    remoteViews.setTextViewText(R.id.tvStatus, "-")


    /*if (totalDBRecords == 0) {
        remoteViews = RemoteViews(context.packageName, R.layout.weather_widget_day)
    } else {
        val model = roomDB.getWeatherDao().getRecordFromPosition(totalDBRecords)
        val modelTemprature: WeatherDetail =
            Gson().fromJson(model.WeatherData, WeatherDetail::class.java)

        var dateHour = convertUTCtoHour24(modelTemprature.current?.dt ?: 0)

        if (dateHour > 17) {
            remoteViews = RemoteViews(context.packageName, R.layout.weather_widget_day)
        } else {
            remoteViews = RemoteViews(context.packageName, R.layout.weather_widget_night)
        }

        remoteViews.setTextViewText(R.id.tvTemprature, modelTemprature.current?.getTemprature())
        remoteViews.setTextViewText(R.id.tvStatus,  modelTemprature.current?.getStatus())

    }
    remoteViews.setOnClickPendingIntent(R.id.llRootWidget, pendingIntent)
    remoteViews.setTextViewText(R.id.tvTemprature, "-")
    remoteViews.setTextViewText(R.id.tvStatus, "-")
*/
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
}