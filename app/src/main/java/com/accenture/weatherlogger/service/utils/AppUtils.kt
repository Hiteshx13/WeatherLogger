package com.accenture.weatherlogger.service.utils

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Handler
import android.provider.Settings
import android.text.format.DateFormat
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.accenture.weatherlogger.R
import com.bumptech.glide.Glide
import java.util.*
import kotlin.math.roundToInt


var BASE_URL_IMAGE = "http://openweathermap.org/img/w/"
var ID_WEATHER_APP = "235bef5a99d6bc6193525182c409602c"
var IMAGE_EXTENSION = ".png"
var WEATHER_DETAILS = "WEATHER_DETAILS"
var ANIM_TIME_IMAGE: Long = 5000
var ANIM_TIME_TEXT: Long = 8000
var ANIM_TIME_300: Long = 8000

fun lounchActivity(activity: AppCompatActivity, intent: Intent) {
    activity.startActivity(intent)
    activity.overridePendingTransition(
        R.anim.slide_from_right,
        R.anim.slide_to_left
    )
}

fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun openAppSettings(activity: Activity) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", activity.packageName, null)
    )
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    activity.startActivity(intent)

}

fun openSettings(context: Context) {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.setClassName(
        "com.android.phone",
        "com.android.phone.NetworkSetting"
    )
    context.startActivity(intent)
}

fun convertUTCtoDateTime(timestamp: Int): String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = timestamp * 1000L
    val format = "dd-MM-yyyy HH:mm"
    return DateFormat.format(format, calendar).toString()
}

fun convertUTCtoTime(timestamp: Int): String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = timestamp * 1000L
    val format = "hh:mm a"
    return DateFormat.format(format, calendar).toString()
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected

}

fun getDayOfWeek(timestamp: Int): String {
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = timestamp * 1000L
    val format = "EEEE"
    return DateFormat.format(format, calendar).toString()
}

fun convertTemprature(temp: Double?): String {
    if (temp == null) {
        return ""
    } else {
        val celc = (temp - 273.15)
        return "${celc.roundToInt()}°C"
    }
}

fun loadImage(context: Context, imageView: ImageView, imgName: String?) {

    imgName?.let{
        val url = BASE_URL_IMAGE + imgName + IMAGE_EXTENSION
        Glide
            .with(context)
            .load(url)
            .fitCenter()
            .placeholder(R.drawable.ic_cloud)
            .into(imageView)
    }

}


fun animateFlip(context: Context, view: View, millisecond: Long) {
    val animFlip =
        AnimatorInflater.loadAnimator(context, R.animator.anim_flip) as ObjectAnimator

    val handler = Handler()
    handler.postDelayed(object : Runnable {
        override fun run() {
            animFlip.target = view
            animFlip.duration = millisecond
            animFlip.start()
            handler.postDelayed(this, millisecond)
        }
    }, 0)
}

fun animateFromRightFull(context: Context, view: View, millisecond: Long) {

    val handler = Handler()
    handler.postDelayed(object : Runnable {
        override fun run() {
            SlideAnimationUtil()
                .slideInFromRightFull(context, view, millisecond)
            handler.postDelayed(this, millisecond)
        }
    }, 0)
}