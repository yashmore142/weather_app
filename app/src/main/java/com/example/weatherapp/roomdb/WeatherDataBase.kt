package com.example.weatherapp.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.ui.dashboard.model.data_insert_room.WeatherRoomData

@Database(entities = [WeatherRoomData::class], version = 1, exportSchema = false)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {

        @Volatile
        private var INSTANCE: WeatherDataBase? = null

        fun getDataBase(context: Context): WeatherDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDataBase::class.java,
                    "tbl_weather"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}