package com.example.weatherapp.ui.dashboard.view.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.HourlyForecastItemBinding
import com.example.weatherapp.ui.dashboard.model.hourely_forecast_response.DataList
import com.example.weatherapp.utils.Constants

class HourlyForecastAdapter(var context: Context, var list: List<DataList>) :
    RecyclerView.Adapter<HourlyForecastAdapter.HourlyHolder>() {
    class HourlyHolder constructor(var binding: HourlyForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyHolder {
        val inflater = LayoutInflater.from(context)
        val binding = HourlyForecastItemBinding.inflate(inflater, parent, false)
        return HourlyHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HourlyHolder, position: Int) {
        holder.binding.txtTemp.setText(Constants.tempKelvinToCelsius(list[position].main.temp))
        Glide.with(context).load(Uri.parse(Constants.IMAGE_BASE_URL +list[position].weather[0].icon+".png"))
            .into(holder.binding.imgTemp)
        holder.binding.txtTime.setText(Constants.extractDateTime(list[position].dt_txt).second)
    }
}