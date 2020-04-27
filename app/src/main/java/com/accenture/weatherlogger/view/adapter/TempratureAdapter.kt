package com.accenture.weatherlogger.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.accenture.weatherlogger.R
import com.accenture.weatherlogger.databinding.RowTempratureListBinding
import com.accenture.weatherlogger.service.database.table.WeatherDetailString
import com.accenture.weatherlogger.service.retrofit.pojo.response.Weather
import com.accenture.weatherlogger.service.retrofit.pojo.response.WeatherDetail
import com.accenture.weatherlogger.service.utils.ANIM_TIME_IMAGE
import com.accenture.weatherlogger.service.utils.ANIM_TIME_TEXT
import com.accenture.weatherlogger.service.utils.animateFlip
import com.accenture.weatherlogger.service.utils.loadImage
import com.accenture.weatherlogger.view.callback.ItemDeleteListener
import com.accenture.weatherlogger.view.callback.PositionClickListener
import com.google.gson.Gson


class TempratureAdapter(
    var context: Context,
    var arrayWeather: ArrayList<WeatherDetailString>,
    var listener: PositionClickListener,
    var itemDeleteListener: ItemDeleteListener
) : RecyclerView.Adapter<TempratureAdapter.ViewHolder>() {

    fun addNewItem(item: WeatherDetailString) {
        arrayWeather.add(item)
       this.notifyItemInserted(arrayWeather.size - 1)
    }

    open class ViewHolder(var binding: RowTempratureListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflator = LayoutInflater.from(context)
        val binding: RowTempratureListBinding =
            DataBindingUtil.inflate(inflator, R.layout.row_temprature_list, parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = arrayWeather[position]
        val modelTemprature: WeatherDetail =
            Gson().fromJson(model.WeatherData, WeatherDetail::class.java)

        holder.binding.model = modelTemprature
        val listWeather: List<Weather?>? = modelTemprature.current?.weather

        if (!listWeather.isNullOrEmpty()) {
            loadImage(
                context,
                holder.binding.imgWeatherIcon,
                listWeather[0]!!.icon
            )
        }
        if (!model.isAnimation) {
            model.isAnimation = true
            animateFlip(
                context,
                holder.binding.imgWeatherIcon,
                ANIM_TIME_IMAGE
            )
            animateFlip(
                context,
                holder.binding.tvLocation,
                ANIM_TIME_TEXT
            )
        }

        holder.binding.tvMoreDetails.setOnClickListener {
            listener.onClick(holder.adapterPosition, modelTemprature)
        }

        holder.binding.tvDelete.setOnClickListener {
            itemDeleteListener.onDelete(position)
        }
    }

    override fun getItemCount(): Int {
        return arrayWeather.size
    }

}