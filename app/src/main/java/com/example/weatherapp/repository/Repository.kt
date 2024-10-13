package com.example.weatherapp.repository

import com.example.weatherapp.ui.dashboard.model.dashboardweather_response.DashboardWeatherResponse
import com.example.weatherapp.ui.dashboard.model.hourely_forecast_response.HourlyForecastResponse

interface Repository {
    suspend fun getDashboardWeatherData(
        lat: String,
        lon: String,
        appId: String
    ): DashboardWeatherResponse
    suspend fun getHourlyForecastData(
        lat: String,
        lon: String,
        appId: String
    ): HourlyForecastResponse
}