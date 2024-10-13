package com.example.weatherapp.repository
import com.example.weatherapp.network.APIInterface
import com.example.weatherapp.ui.dashboard.model.dashboardweather_response.DashboardWeatherResponse
import com.example.weatherapp.ui.dashboard.model.hourely_forecast_response.HourlyForecastResponse

class RepositoryImpl(
    private val apiInterface: APIInterface
) : Repository {

    override suspend fun getDashboardWeatherData(
        lat: String,
        lon: String,
        appId: String
    ): DashboardWeatherResponse = apiInterface.getDashboardWeatherData(lat, lon, appId)

    override suspend fun getHourlyForecastData(
        lat: String,
        lon: String,
        appId: String
    ): HourlyForecastResponse = apiInterface.getHourlyForecastData(lat,lon,appId)

}

