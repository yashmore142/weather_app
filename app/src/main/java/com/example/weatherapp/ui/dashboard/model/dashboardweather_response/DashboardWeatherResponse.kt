package com.example.weatherapp.ui.dashboard.model.dashboardweather_response

data class DashboardWeatherResponse(
    var base: String = "",
    var clouds: Clouds = Clouds(),
    var cod: Int = 0,
    var coord: Coord = Coord(),
    var dt: Int = 0,
    var id: Int = 0,
    var main: Main = Main(),
    var name: String = "",
    var rain: Rain = Rain(),
    var sys: Sys = Sys(),
    var timezone: Int = 0,
    var visibility: Int = 0,
    var weather: List<Weather> = listOf(),
    var wind: Wind = Wind()
)