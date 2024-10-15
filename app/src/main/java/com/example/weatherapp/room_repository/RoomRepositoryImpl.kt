package com.example.weatherapp.room_repository

import com.example.weatherapp.roomdb.WeatherDao
import com.example.weatherapp.ui.dashboard.model.data_insert_room.WeatherRoomData
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(private val weatherDao: WeatherDao
) : RoomRepository {
    override suspend fun getWeather(): List<WeatherRoomData> = weatherDao.getAllData()

    override suspend fun addData(weather: WeatherRoomData){
        weatherDao.addWeather(weather)
    }

    override suspend fun deleteData(weather: WeatherRoomData) {
        weatherDao.deleteWeather(weather)
    }
}