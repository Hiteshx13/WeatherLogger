package com.accenture.weatherlogger.view.activity

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.accenture.weatherlogger.R
import com.accenture.weatherlogger.databinding.ActivityMainBinding
import com.accenture.weatherlogger.service.database.table.WeatherDetailString
import com.accenture.weatherlogger.service.retrofit.pojo.request.ReqWeather
import com.accenture.weatherlogger.service.retrofit.pojo.response.WeatherDetail
import com.accenture.weatherlogger.service.utils.*
import com.accenture.weatherlogger.view.adapter.TempratureAdapter
import com.accenture.weatherlogger.view.callback.ItemClickListener
import com.accenture.weatherlogger.view.callback.ItemDeleteListener
import com.accenture.weatherlogger.view.callback.PositionClickListener
import com.accenture.weatherlogger.viewmodel.WeatherListViewModel
import com.google.gson.Gson


class MainActivity : BaseActivity<ActivityMainBinding>(), LocationListener,
    LocationUtils.LocationListener {

    lateinit var adapter: TempratureAdapter
    lateinit var location: Location
    /** IF DEVICE LOCATION IS NOT ENABLED THAN BELOW LOCATION WILL BE USED TO GET WEATHER DETAILS**/
    private var LATITUDE = "56.9512938"
    private var LONGITUDE = "24.0958929"
    var apiCallRepeatTime: Long = 0
    /** BELOW VARIABLE DECIDE TO AUTO-CALL API OR NOT AT EVERY REPEATED TIME DURATION**/
    private var isAutoCallBlocked = false
    var isResponseDialogOpen = false
    /**HANDLER TO AUTO-CALL API**/
    var handlerAutoCall = Handler()
    lateinit var viewModel: WeatherListViewModel
    private var locationUtils: LocationUtils? = null
    var arrayWeather = ArrayList<WeatherDetailString>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView(this@MainActivity, R.layout.activity_main)
        viewModel =
            ViewModelProviders.of(this).get(WeatherListViewModel::class.java)
        initialization()
        observeViewModel(viewModel)
    }

    /**DATA INITIALIZATION**/
    private fun initialization() {
        /****/
        apiCallRepeatTime = resources.getInteger(R.integer.app_auto_refresh_time).toLong()
        arrayWeather = roomDB.getWeatherDao().getAllRecords() as ArrayList<WeatherDetailString>
        locationUtils =
            LocationUtils(this, this)
        locationUtils?.init()
        adapter =
            TempratureAdapter(
                this@MainActivity,
                arrayWeather,
                object : PositionClickListener {
                    override fun onClick(pos: Int, model: WeatherDetail) {
                        lounchActivity(
                            this@MainActivity,
                            WeatherDetailsActivity.getIntent(
                                applicationContext,
                                model
                            )
                        )
                    }
                },
                object :
                    ItemDeleteListener {
                    override fun onDelete(pos: Int) {

                        if (arrayWeather.size > 0) {
                            roomDB.getWeatherDao().deleteByPosition(arrayWeather[pos].IDWeather)
                            arrayWeather.removeAt(pos)
                            adapter.notifyDataSetChanged()
                        }
                        updateView()
                    }
                })
        updateView()
        /**set weather data adapter**/
        binding.rvWeather.layoutManager = LinearLayoutManager(this)
        binding.rvWeather.adapter = adapter
    }

    /** API call response observer**/
    private fun observeViewModel(viewModel: WeatherListViewModel) {
        viewModel.getWeatherData()
            .observe(this, Observer<WeatherDetail> { response: WeatherDetail? ->
                if (response?.isSuccess == false) {
                    showProgress(false)
                    isAutoCallBlocked = false
                } else {
                    showProgress(false)
                    if (response != null) {
                        /**checking if dialog is open or not to block API call until current dialog will be closed**/
                        isResponseDialogOpen = true
                        showWeatherDialog(
                            this@MainActivity,
                            response,
                            object :
                                ItemClickListener {
                                override fun onClick(status: Boolean) {
                                    /**checking if dialog is open or not to block API call untill current dialog will be closed**/
                                    isResponseDialogOpen = false
                                    if (status) {
                                        val strResponse = Gson().toJson(response)
                                        var weatherData =
                                            WeatherDetailString(
                                                WeatherData = strResponse,
                                                deviceTime = getCurrentDeviceTime()
                                            )
                                        roomDB.getWeatherDao()
                                            .insertData(weatherData)
                                        adapter.addNewItem(weatherData)
                                        binding.rvWeather.scrollToPosition(arrayWeather.size - 1)
                                        updateView()
                                        /** updating widget data**/
                                        // updateWidget()
                                        sendUpdateBroadcast()
                                    }
                                    setAutoCall(isAutoCallBlocked)

                                    /** unblock auto api call**/
                                    isAutoCallBlocked = false
                                }
                            })
                    } else {

                        if (response?.apiResponseMessage?.isEmpty() == true) {
                            showToast(
                                this@MainActivity,
                                response!!.apiResponseMessage
                            )
                        }
                    }
                }
            })
    }

    /**show dialog to ask permission for location  **/
    private fun showPermissionGrantDialog() {
        showMessageDialog(
            this,
            this.getString(R.string.location_permission_not_allowed),
            object :
                ItemClickListener {
                override fun onClick(status: Boolean) {
                    if (status) {
                        openAppSettings(
                            this@MainActivity
                        )
                    } else {
                        callWeatherDetailsApi()
                    }
                }
            })
    }

    private fun showProgress(isShow: Boolean) {
        if (isShow) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }

    fun updateView() {
        /**set view visiblity based on data**/
        binding.tvNoData.visibility = if (arrayWeather.size == 0) View.VISIBLE else View.GONE
        binding.rvWeather.visibility = if (arrayWeather.size == 0) View.GONE else View.VISIBLE

    }

    private fun callWeatherDetailsApi() {
        showProgress(true)
        viewModel.getWeatherDetails(
            ReqWeather(
                LATITUDE,
                LONGITUDE,
                ID_WEATHER_APP
            )
        )
    }

    fun setAutoCall(isCallBlocked: Boolean) {
        /** re-start auto call if blocked**/
        if (isCallBlocked) {
            handlerAutoCall.postDelayed(object : Runnable {
                override fun run() {
                    /**checking if dialog is open than skip API call untill current dialog will be closed**/
                    if (!isAutoCallBlocked) {
                        if (!isResponseDialogOpen && isNetworkAvailable(
                                this@MainActivity
                            )
                        ) {
                            callWeatherDetailsApi()
                        }
                        handlerAutoCall.postDelayed(this, apiCallRepeatTime)
                    }
                }
            }, apiCallRepeatTime)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LocationUtils.REQUEST_LOCATION_SETTINGS) {
            locationUtils?.onActivityResult(requestCode, resultCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LocationUtils.LOCATION_PERMISSION_REQUEST_CODE -> {
                locationUtils?.onRequestPermissionsResult(requestCode, grantResults)
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    callWeatherDetailsApi()
                } else {
                    showPermissionGrantDialog()
                }
            }
        }
    }

    override fun onLocationReceived(location: Location?) {
        location?.let {
            this.location = location
            LATITUDE = it.latitude.toString()
            LONGITUDE = it.longitude.toString()
            Log.i("#Location Lat: $LATITUDE: ", "Lon: $LONGITUDE")
        }
    }

    override fun onLocationChanged(location: Location?) {
        location?.let {
            this.location = location
            LATITUDE = it.latitude.toString()
            LONGITUDE = it.longitude.toString()
            Log.i("#Location Lat: $LATITUDE: ", "Lon: $LONGITUDE")
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.i("#Location info:", "onStatusChanged")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.i("#Location info:", "onProviderEnabled")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.i("#Location info:", "onProviderDisabled")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                if (isNetworkAvailable(
                        this@MainActivity
                    )
                ) {
                    /** block another api call  until current api call complete**/
                    isAutoCallBlocked = true
                    handlerAutoCall.removeCallbacksAndMessages(null)
                    when {

                        locationUtils?.isLocationPermissionAllowed() == true -> {
                            callWeatherDetailsApi()
                        }
                        else -> {
                            showPermissionGrantDialog()
                        }
                    }
                } else {
                    /**DIALOG FOR NOTIFY USER ABOUT DEVICE INTERNET CONNECTIVITY NOT ON **/
                    showMessageDialog(
                        this,
                        resources.getString(R.string.no_internet),
                        object :
                            ItemClickListener {
                            override fun onClick(status: Boolean) {
                                if (status) {
                                    openSettings(
                                        this@MainActivity
                                    )
                                }
                            }
                        })
                }
            }
        }
        return true
    }

    fun sendUpdateBroadcast() {
        val intent = Intent(this, WeatherWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val name = ComponentName(this@MainActivity, WeatherWidget::class.java)
        val ids = AppWidgetManager.getInstance(this@MainActivity).getAppWidgetIds(name)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
    }
}