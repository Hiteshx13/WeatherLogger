package com.hitesh.weatherlogger.service.utils

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.hitesh.weatherlogger.R
import com.hitesh.weatherlogger.databinding.DialogMessageBinding
import com.hitesh.weatherlogger.databinding.DialogShowTempratureBinding
import com.hitesh.weatherlogger.service.retrofit.pojo.response.Weather
import com.hitesh.weatherlogger.service.retrofit.pojo.response.WeatherDetail
import com.hitesh.weatherlogger.view.callback.ItemClickListener

fun showWeatherDialog(
    context: Context,
    weatherDetail: WeatherDetail,
    listener: ItemClickListener
): Dialog? {

    val inflator = LayoutInflater.from(context)
    val binding: DialogShowTempratureBinding =
        DataBindingUtil.inflate(inflator, R.layout.dialog_show_temprature, null, false)

    val mDialog: Dialog? = Dialog(context)
    mDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog?.setContentView(binding.root)
    mDialog?.setCanceledOnTouchOutside(false)
    mDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

    animateFlip(
        context,
        binding.imgWeatherIcon,
        ANIM_TIME_IMAGE
    )
    animateFlip(
        context,
        binding.tvLocation,
        ANIM_TIME_TEXT
    )

    binding.model = weatherDetail
    val listWeather: List<Weather?>? = weatherDetail.current?.weather
    if (!listWeather.isNullOrEmpty()) {
        loadImage(
            context,
            binding.imgWeatherIcon,
            listWeather[0]!!.icon
        )
    }

    binding.ivSave.setOnClickListener {
        listener.onClick(true)
        mDialog?.dismiss()
    }

    binding.ivCancel.setOnClickListener {
        listener.onClick(false)
        mDialog?.dismiss()
    }


    /**handler to perform auto click after 10 second if dialog is visible**/
    val handler = Handler()
    handler.postDelayed({
        handler.removeCallbacksAndMessages(null)
        if(mDialog?.isShowing==true){
            binding.ivSave.performClick()
        }
    }, 10000)

    mDialog?.show()

    return mDialog
}


fun showMessageDialog(context: Context, message: String, listener: ItemClickListener): Dialog? {

    val inflator = LayoutInflater.from(context)
    val binding: DialogMessageBinding =
        DataBindingUtil.inflate(inflator, R.layout.dialog_message, null, false)

    val mDialog: Dialog? = Dialog(context)
    mDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
    mDialog?.setContentView(binding.root)
    mDialog?.setCanceledOnTouchOutside(false)
    mDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

    binding.tvMessage.text = message

    animateFromRightFull(
        context,
        binding.llHeader,
        5000
    )

    binding.ivSave.setOnClickListener {
        listener.onClick(true)
        mDialog?.dismiss()
    }
    binding.ivCancel.setOnClickListener {
        listener.onClick(false)
        mDialog?.dismiss()
    }

    mDialog?.show()
    return mDialog
}
