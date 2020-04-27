package com.hitesh.weatherlogger.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.hitesh.weatherlogger.R
import com.hitesh.weatherlogger.databinding.RowTempratureHourlyBinding
import com.hitesh.weatherlogger.service.retrofit.pojo.response.Hourly
import com.hitesh.weatherlogger.service.retrofit.pojo.response.Weather
import com.hitesh.weatherlogger.service.utils.ANIM_TIME_IMAGE
import com.hitesh.weatherlogger.service.utils.animateFlip
import com.hitesh.weatherlogger.service.utils.loadImage

class HourlyTempratureAdapter(
    var context: Context,
    var arrayHourly: List<Hourly?>?
) : RecyclerView.Adapter<HourlyTempratureAdapter.ViewHolder>() {

    open class ViewHolder(var binding: RowTempratureHourlyBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(context)
        val binding: RowTempratureHourlyBinding =
            DataBindingUtil.inflate(inflator, R.layout.row_temprature_hourly, parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val modelHourly: Hourly = arrayHourly?.get(position) ?: Hourly()
        val listWeather: List<Weather?>? = modelHourly.weather

        if (!listWeather.isNullOrEmpty()) {
            loadImage(
                context,
                holder.binding.imgWeatherIcon,
                listWeather[0]!!.icon
            )
        }

        holder.binding.model = modelHourly
        if (!modelHourly.isAnimation) {
            arrayHourly?.get(position)?.isAnimation = true
            animateFlip(
                context,
                holder.binding.imgWeatherIcon,
                ANIM_TIME_IMAGE
            )
            animateFlip(
                context,
                holder.binding.tvStatus,
                ANIM_TIME_IMAGE
            )
        }
    }

    override fun getItemCount(): Int {
        return arrayHourly?.size ?: 0
    }
}