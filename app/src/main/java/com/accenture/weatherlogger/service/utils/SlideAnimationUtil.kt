package com.accenture.weatherlogger.service.utils

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import com.accenture.weatherlogger.R

class SlideAnimationUtil {
    fun slideInFromRightFull(context: Context, view: View, millisecond: Long) {
        val slide =
            AnimationUtils.loadAnimation(context, R.anim.slide_from_right_full)
        slide.duration = millisecond
        view.startAnimation(slide)
    }
}