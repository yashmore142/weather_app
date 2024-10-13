package com.example.weatherapp.ui.favouriteweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.room_repository.RoomRepository
import com.example.weatherapp.roomdb.WeatherDao
import com.example.weatherapp.ui.dashboard.model.data_insert_room.WeatherRoomData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class RoomFavouriteViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {
    var homeErrorData: MutableLiveData<String> = MutableLiveData("")
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        homeErrorData.value = exception.localizedMessage
    }
    private var getFavouriteJob: Job? = null
    private var _responseGetWeather: MutableLiveData<List<WeatherRoomData>> =
        MutableLiveData(emptyList())
    val responseGetWeather: LiveData<List<WeatherRoomData>> = _responseGetWeather

    fun getWeather() {
        getFavouriteJob?.cancel()

        getFavouriteJob = viewModelScope.launch(coroutineExceptionHandler) {
            try {
                val response = withContext(Dispatchers.IO) {
                    roomRepository.getWeather()
                }
                _responseGetWeather.value = response
            } catch (e: Exception) {
                throw e
            }
        }
    }

}