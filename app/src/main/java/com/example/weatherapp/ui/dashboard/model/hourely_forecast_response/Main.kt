package com.example.weatherapp.ui.dashboard.model.hourely_forecast_response

data class Main(
    var feels_like: Double = 0.0,
    var grnd_level: Int = 0,
    var humidity: Int = 0,
    var pressure: Int = 0,
    var sea_level: Int = 0,
    var temp: Double = 0.0,
    var temp_kf: Double = 0.0,
    var temp_max: Double = 0.0,
    var temp_min: Double = 0.0
)