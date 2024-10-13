package com.example.weatherapp.ui.dashboard.model.hourely_forecast_response

data class DataList (
    var clouds: Clouds = Clouds(),
    var dt: Int = 0,
    var dt_txt: String = "",
    var main: Main = Main(),
    var pop: Double = 0.0,
    var rain: Rain = Rain(),
    var sys: Sys = Sys(),
    var visibility: Int = 0,
    var weather: List<Weather> = listOf(),
    var wind: Wind = Wind()
)