package com.example.weatherapp.ui.favouriteweather.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityFavouriteWeatherBinding
import com.example.weatherapp.ui.dashboard.model.data_insert_room.WeatherRoomData
import com.example.weatherapp.ui.dashboard.view.activity.MainActivity
import com.example.weatherapp.ui.dashboard.view.adapter.HourlyForecastAdapter
import com.example.weatherapp.ui.dashboard.viewmodel.DashBoardViewModel
import com.example.weatherapp.ui.favouriteweather.adapter.CitiesAdapter
import com.example.weatherapp.ui.favouriteweather.interfaces.DeleteWeatherClick
import com.example.weatherapp.ui.favouriteweather.viewmodel.RoomFavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteWeatherActivity : AppCompatActivity(), DeleteWeatherClick {
    private val mRoomViewModel by viewModels<RoomFavouriteViewModel>()
    var layoutManager: LinearLayoutManager? = null
    private lateinit var citiesAdapter: CitiesAdapter
    private var list: List<WeatherRoomData> = listOf()

    private lateinit var mBinding: ActivityFavouriteWeatherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_favourite_weather)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.rvCities.layoutManager = layoutManager
        observer()
        mBinding.imgBack.setOnClickListener {
            onBackPressed()
        }
        mRoomViewModel.getWeather()
    }

    private fun observer() {
        mRoomViewModel.responseGetWeather.observe(this) {
            if (!it.isEmpty()) {
                list.toMutableList().clear()
                list = it
                citiesAdapter = CitiesAdapter(this, list, this)
                mBinding.rvCities.adapter = citiesAdapter
            }

        }
    }

    override fun deleteClick(weatherRoomData: WeatherRoomData) {
        mRoomViewModel.deleteWeather(weatherRoomData)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            mRoomViewModel.getWeather()
        }, 1000)

    }
}