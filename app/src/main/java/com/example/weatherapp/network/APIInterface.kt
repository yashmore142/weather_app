package com.example.weatherapp.network

import com.example.weatherapp.ui.dashboard.model.dashboardweather_response.DashboardWeatherResponse
import com.example.weatherapp.ui.dashboard.model.hourely_forecast_response.HourlyForecastResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface APIInterface {

    @POST("2.5/weather")
    suspend fun getDashboardWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
    ): DashboardWeatherResponse

    @GET("2.5/forecast")
    suspend fun getHourlyForecastData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
    ): HourlyForecastResponse
}
