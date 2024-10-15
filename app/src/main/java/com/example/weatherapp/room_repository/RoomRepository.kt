package com.example.weatherapp.room_repository

import com.example.weatherapp.ui.dashboard.model.data_insert_room.WeatherRoomData

interface RoomRepository {
    suspend fun getWeather () :List<WeatherRoomData>

    suspend fun addData(weather: WeatherRoomData)
    suspend fun deleteData(weather: WeatherRoomData)
}