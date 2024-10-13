package com.example.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Constants {
    const val BASE_URL: String = "https://api.openweathermap.org/data/"

    const val IMAGE_BASE_URL: String = "http://openweathermap.org/img/wn/"
    const val API_KEY: String = "92aee903951597d3a0d9526616d208ce"
    //const val API_KEY: String = "e5db94fcca257165c5e6ca3611c1fa2d"

    fun tempKelvinToCelsius(temp: Double): String {
        return (temp - 273.15).toInt().toString() + "°"
    }

    fun tempKelvinToFahrenheit(temp: Double): Double {
        val celsius = temp - 273.15
        return (celsius * 9 / 5) + 32
    }

    fun extractDateTime(datetime: String): Pair<String, String> {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date: Date? = inputFormat.parse(datetime)

        return if (date != null) {
            val formattedDate = dateFormat.format(date)
            val formattedTime = timeFormat.format(date)
            Pair(formattedDate, formattedTime)
        } else {
            Pair("", "")
        }
    }

    fun getDayFromNumber(number: Double): String {
        val daysOfWeek =
            listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val dayIndex = (number.toInt() % 7)
        return daysOfWeek[dayIndex]
    }

}