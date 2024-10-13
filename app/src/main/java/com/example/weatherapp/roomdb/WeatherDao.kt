package com.example.weatherapp.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weatherapp.ui.dashboard.model.data_insert_room.WeatherRoomData

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addWeather(data: WeatherRoomData)
    @Query("SELECT * FROM tbl_weather")
    fun getAllData(): List<WeatherRoomData>

    @Delete
    suspend fun deleteWeather(data: WeatherRoomData)

    @Update
    suspend fun updateWeather(data: WeatherRoomData)

}