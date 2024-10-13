package com.example.weatherapp.ui.dashboard.model.data_insert_room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_weather")
data class WeatherRoomData(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var lat: String = "",
    var city: String = "",
    var temp: String = "",
    var minmaxtemp: String = "",
    var humidity: String = "",
    var longitude : String = ""

/*
    var createdAt: String = ""
*/
)