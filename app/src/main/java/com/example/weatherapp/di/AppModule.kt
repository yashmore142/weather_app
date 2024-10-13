package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.network.APIClient
import com.example.weatherapp.network.APIInterface
import com.example.weatherapp.repository.Repository
import com.example.weatherapp.repository.RepositoryImpl
import com.example.weatherapp.room_repository.RoomRepository
import com.example.weatherapp.room_repository.RoomRepositoryImpl
import com.example.weatherapp.roomdb.WeatherDao
import com.example.weatherapp.roomdb.WeatherDataBase
import com.example.weatherapp.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun providesAPIInterface(): APIInterface =
        APIClient.getClient()!!.create(APIInterface::class.java)

    @Provides
    @Singleton
    fun providesRepository(apiInterface: APIInterface): Repository {
        return RepositoryImpl(apiInterface)
    }

    @Provides
    @Singleton
    fun providesSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }
    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherDataBase {
        return WeatherDataBase.getDataBase(context)
    }
    @Provides
    @Singleton
    fun providesWeatherDao(db: WeatherDataBase): WeatherDao {
        return db.weatherDao()
    }

    @Provides
    @Singleton
    fun providesRoomRepository(weatherDao: WeatherDao): RoomRepository {
        return RoomRepositoryImpl(weatherDao)
    }
}