package com.example.weatherapp.ui.dashboard.model.dashboardweather_response

data class Sys(
    var country: String = "",
    var id: Int = 0,
    var sunrise: Int = 0,
    var sunset: Int = 0,
    var type: Int = 0
)