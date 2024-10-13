package com.example.weatherapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.weatherapp.R

class SessionManager(context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val CURRENT_DATA = "current_data"
    }
}