package com.example.weatherapp.ui.dashboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.repository.Repository
import com.example.weatherapp.ui.dashboard.model.dashboardweather_response.DashboardWeatherResponse
import com.example.weatherapp.ui.dashboard.model.hourely_forecast_response.HourlyForecastResponse
import com.example.weatherapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var homeErrorData: MutableLiveData<String> = MutableLiveData("")
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        homeErrorData.value = exception.localizedMessage
    }

    //get weather
    private var getWeatherJob: Job? = null
    private var _responseWeather: MutableLiveData<DashboardWeatherResponse> =
        MutableLiveData(DashboardWeatherResponse())
    val responseWeather: LiveData<DashboardWeatherResponse> = _responseWeather

    //get hourly forecast
    private var getHourlyForecastJob: Job? = null
    private var _responseHourlyForecast: MutableLiveData<HourlyForecastResponse> =
        MutableLiveData(HourlyForecastResponse())
    val responseHourlyForecast: LiveData<HourlyForecastResponse> = _responseHourlyForecast


    fun getWeather(lat: String, lon: String) {
        getWeatherJob?.cancel()

        getWeatherJob = viewModelScope.launch(coroutineExceptionHandler) {
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.getDashboardWeatherData(lat, lon, Constants.API_KEY)
                }
                _responseWeather.value = response
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun getHourlyForecast(lat: String, lon: String) {
        getHourlyForecastJob?.cancel()

        getHourlyForecastJob = viewModelScope.launch(coroutineExceptionHandler) {
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.getHourlyForecastData(lat, lon, Constants.API_KEY)
                }
                _responseHourlyForecast.value = response
            } catch (e: Exception) {
                throw e
            }
        }
    }

}