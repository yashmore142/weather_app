package com.example.weatherapp.ui.dashboard.model.hourely_forecast_response

data class HourlyForecastResponse(
    var city: City = City(),
    var cnt: Int = 0,
    var cod: String = "",
    var list: List<DataList> = listOf(),
    var message: Int = 0
)