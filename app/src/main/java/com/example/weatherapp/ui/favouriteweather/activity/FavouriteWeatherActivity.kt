package com.example.weatherapp.ui.favouriteweather.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityFavouriteWeatherBinding
import com.example.weatherapp.ui.dashboard.view.adapter.HourlyForecastAdapter
import com.example.weatherapp.ui.dashboard.viewmodel.DashBoardViewModel
import com.example.weatherapp.ui.favouriteweather.adapter.CitiesAdapter
import com.example.weatherapp.ui.favouriteweather.viewmodel.RoomFavouriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteWeatherActivity : AppCompatActivity() {
    private val mRoomViewModel by viewModels<RoomFavouriteViewModel>()
    var layoutManager: LinearLayoutManager? = null
    private lateinit var citiesAdapter: CitiesAdapter

    private lateinit var mBinding : ActivityFavouriteWeatherBinding
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
        mRoomViewModel.responseGetWeather.observe(this){
            if (!it.isEmpty()){

                citiesAdapter = CitiesAdapter(this,it)
                mBinding.rvCities.adapter = citiesAdapter
            }

        }
    }
}