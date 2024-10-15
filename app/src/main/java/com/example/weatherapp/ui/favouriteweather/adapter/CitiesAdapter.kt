package com.example.weatherapp.ui.favouriteweather.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.CitiesItemBinding
import com.example.weatherapp.databinding.HourlyForecastItemBinding
import com.example.weatherapp.ui.dashboard.model.data_insert_room.WeatherRoomData
import com.example.weatherapp.ui.dashboard.view.activity.MainActivity
import com.example.weatherapp.ui.dashboard.view.adapter.HourlyForecastAdapter
import com.example.weatherapp.ui.favouriteweather.interfaces.DeleteWeatherClick
import com.example.weatherapp.utils.Constants

class CitiesAdapter(var context: Context, var list: List<WeatherRoomData>,var deleteWeatherClick: DeleteWeatherClick) :
    RecyclerView.Adapter<CitiesAdapter.CitiesHolder>() {
    class CitiesHolder constructor(var binding: CitiesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesHolder {
        val inflater = LayoutInflater.from(context)
        val binding = CitiesItemBinding.inflate(inflater, parent, false)
        return CitiesHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CitiesHolder, position: Int) {
        holder.binding.txtCityName.setText(list[position].city)
        holder.binding.txtCityTemp.setText(list[position].temp)
        holder.binding.txtMinMax.setText(list[position].minmaxtemp)
        holder.binding.txtHumidity.setText("Humidity :" +list[position].humidity + " %")

        holder.binding.imgDelete.setOnClickListener {
            deleteWeatherClick.deleteClick(list[position])
        }
        holder.binding.root.setOnClickListener {
            Log.i("citiesintentData","lat = ${list[position].lat},${list[position].longitude}")
            val intent = Intent(context,MainActivity::class.java)
            intent.putExtra("lat",list[position].lat)
            intent.putExtra("long",list[position].longitude)
            context.startActivity(intent)

        }
    }
}