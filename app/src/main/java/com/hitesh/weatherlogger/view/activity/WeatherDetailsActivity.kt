package com.hitesh.weatherlogger.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.hitesh.weatherlogger.R
import com.hitesh.weatherlogger.databinding.ActivityWeatherDetailsBinding
import com.hitesh.weatherlogger.databinding.RowTempratureDailyBinding
import com.hitesh.weatherlogger.service.retrofit.pojo.response.Weather
import com.hitesh.weatherlogger.service.retrofit.pojo.response.WeatherDetail
import com.hitesh.weatherlogger.service.utils.*
import com.hitesh.weatherlogger.view.adapter.HourlyTempratureAdapter

class WeatherDetailsActivity : BaseActivity<ActivityWeatherDetailsBinding>() {

    companion object {
        fun getIntent(context: Context, weatherDetail: WeatherDetail): Intent {
            val intent = Intent(context, WeatherDetailsActivity::class.java)
            intent.putExtra(WEATHER_DETAILS, weatherDetail)
            return intent
        }
    }

    var weatherDetail: WeatherDetail? = null
    lateinit var adapter: HourlyTempratureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(this@WeatherDetailsActivity, R.layout.activity_weather_details)

        weatherDetail = intent.getParcelableExtra(WEATHER_DETAILS) as WeatherDetail
        binding.model = weatherDetail

        /**Animate view**/
        animateFlip(
            this,
            binding.tvLocation,
            ANIM_TIME_TEXT
        )
        animateFromRightFull(
            this,
            binding.tvSunrise,
            ANIM_TIME_300
        )

        /**Load image**/
        val listWeather: List<Weather?>? = weatherDetail?.current?.weather
        if (!listWeather.isNullOrEmpty()) {
            loadImage(
                this,
                binding.imgWeatherIcon,
                listWeather[0]!!.icon
            )
        }

        /**inflate daily weather data in horizontal scroll view**/
        inflateDailyData()
        if (!weatherDetail?.hourly.isNullOrEmpty()) {
            adapter =
                HourlyTempratureAdapter(
                    this,
                    weatherDetail?.hourly
                )
            binding.rvHourly.layoutManager = LinearLayoutManager(this)
            binding.rvHourly.adapter = adapter
        }

        binding.progressbar.visibility = View.GONE
        binding.llData.visibility = View.VISIBLE
    }

    private fun inflateDailyData() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        val inflater: LayoutInflater =
            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        weatherDetail?.daily?.forEach {
            val rowBinding = DataBindingUtil.inflate<RowTempratureDailyBinding>(
                inflater,
                R.layout.row_temprature_daily, null, false
            )
            rowBinding.model = it

            rowBinding.root.layoutParams = LinearLayout.LayoutParams(
                ((width / 4) * 3),
                WindowManager.LayoutParams.WRAP_CONTENT
            )

            val listWeather: List<Weather?>? = it?.weather
            if (!listWeather.isNullOrEmpty()) {
                loadImage(
                    this,
                    rowBinding.imgWeatherIcon,
                    listWeather[0]!!.icon
                )
            }

            binding.root.setBackgroundColor(resources.getColor(android.R.color.transparent))
            binding.llDaily.addView(rowBinding.root)
        }

    }
}