package com.example.weatherapp.ui.dashboard.model.hourely_forecast_response

data class City(
    var coord: Coord = Coord(),
    var country: String = "",
    var id: Int = 0,
    var name: String = "",
    var population: Int = 0,
    var sunrise: Int = 0,
    var sunset: Int = 0,
    var timezone: Int = 0
)