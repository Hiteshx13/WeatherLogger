package com.hitesh.weatherlogger.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.hitesh.weatherlogger.R
import com.hitesh.weatherlogger.service.database.AppDatabase
import com.hitesh.weatherlogger.service.retrofit.ApiInterface
import com.hitesh.weatherlogger.service.retrofit.RetrofitClient


open class BaseActivity<V : ViewDataBinding> : AppCompatActivity() {

    lateinit var roomDB: AppDatabase
    var service: ApiInterface? = null
    lateinit var binding: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialization()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    fun bindView(activity: AppCompatActivity, layout: Int) {
        binding = DataBindingUtil.setContentView(activity, layout)
    }

    private fun initialization() {
        roomDB = AppDatabase.getDatabase(this)
        service = RetrofitClient.getRetrofitInstance().create(
            ApiInterface::class.java
        )
    }
}