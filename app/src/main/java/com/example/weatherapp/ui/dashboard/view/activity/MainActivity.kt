package com.example.weatherapp.ui.dashboard.view.activity

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.ui.dashboard.model.data_insert_room.WeatherRoomData
import com.example.weatherapp.ui.dashboard.model.hourely_forecast_response.DataList
import com.example.weatherapp.ui.dashboard.view.adapter.HourlyForecastAdapter
import com.example.weatherapp.ui.dashboard.viewmodel.DashBoardViewModel
import com.example.weatherapp.ui.dashboard.viewmodel.RoomDashboardViewModel
import com.example.weatherapp.ui.favouriteweather.activity.FavouriteWeatherActivity
import com.example.weatherapp.utils.Constants.tempKelvinToCelsius
import com.example.weatherapp.utils.SessionManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var hourlyAdapter: HourlyForecastAdapter
    private var list: List<DataList> = listOf()
    private val weatherRoomData = WeatherRoomData()
    var layoutManager: LinearLayoutManager? = null
    private var lat = ""
    private var lon = ""

    @Inject
    lateinit var sessionManager: SessionManager
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    val textwatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (getLatLongFromCity(p1.toString()) != null) {
                apiCall(
                    getLatLongFromCity(p1.toString())!!.first.toString(),
                    getLatLongFromCity(p1.toString())!!.second.toString()
                )
            }

        }

        override fun afterTextChanged(p0: Editable?) {

        }

    }

    private val mViewModel by viewModels<DashBoardViewModel>()
    private val mRoomViewModel by viewModels<RoomDashboardViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.rvHourlyForecast.layoutManager = layoutManager
        mBinding.sihmmer.visibility = View.VISIBLE
        mBinding.llMain.visibility = View.GONE
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val extras = intent.extras
        if (extras != null && extras.containsKey("lat") && extras.containsKey("long")) {
            val latitude = intent.getStringExtra("lat")
            val long = intent.getStringExtra("long")
            Log.i("intentData", "lat = ${latitude},${long}")
            apiCall(latitude.toString(), long.toString())
        } else {
            locationPermissionRequest = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        enableGPS()
                    }

                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        enableGPS()
                    }

                    else -> {
                        Toast.makeText(
                            this,
                            getString(R.string.location_permissions_are_required_to_use_this_feature),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            requestLocationPermissions()
        }
        observer()
        mBinding.edtCity.addTextChangedListener(textwatcher)
        mBinding.imgMenu.setOnClickListener {
            startActivity(Intent(this, FavouriteWeatherActivity::class.java))
        }
        mBinding.imgFavourite.setOnClickListener {
            mBinding.imgFavourite.setImageResource(R.drawable.ic_favourite_fiil)
            mRoomViewModel.addFavouriteWeather(weatherRoomData)
        }
    }

    fun getLatLongFromCity(city: String): Pair<Double, Double>? {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocationName(city, 1)
            if (addresses!!.isNotEmpty()) {
                val latitude = addresses[0]!!.latitude
                val longitude = addresses[0]!!.longitude
                Toast.makeText(this, "lat =$latitude, long = $longitude", Toast.LENGTH_LONG).show()
                Pair(latitude, longitude)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun observer() {
        mRoomViewModel.responseAddWeather.observe(this) {
            Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
        }

        mViewModel.homeErrorData.observe(this) {
            Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
        }
        mViewModel.responseHourlyForecast.observe(this) { weatherResponse ->
            if (weatherResponse.list.isNotEmpty()) {
                mBinding.sihmmer.visibility = View.GONE
                mBinding.llMain.visibility = View.VISIBLE
                val mainCondition = weatherResponse.list[0].weather[0].main

                when (mainCondition) {
                    "Clear" -> mBinding.llMain.setBackgroundResource(R.drawable.clear)
                    "Rain" -> mBinding.llMain.setBackgroundResource(R.drawable.rainy)
                    "Clouds" -> mBinding.llMain.setBackgroundResource(R.drawable.cloud)
                    "Snow" -> mBinding.llMain.setBackgroundResource(R.drawable.snow)
                    else -> mBinding.llMain.setBackgroundResource(R.drawable.sunny_img)
                }
                weatherRoomData.lat = weatherResponse.city.coord.lat.toString()
                weatherRoomData.longitude = weatherResponse.city.coord.lon.toString()
                weatherRoomData.city = weatherResponse.city.name
                weatherRoomData.minmaxtemp =
                    "${tempKelvinToCelsius(weatherResponse.list[0].main.temp_max)} / ${
                        tempKelvinToCelsius(
                            weatherResponse.list[0].main.temp_min
                        )
                    }"
                weatherRoomData.temp = tempKelvinToCelsius(weatherResponse.list[0].main.temp)
                weatherRoomData.humidity = weatherResponse.list[0].main.humidity.toString()
                mBinding.txtTemp.setText(tempKelvinToCelsius(weatherResponse.list[0].main.temp))
                mBinding.edtCity.setText(weatherResponse.city.name)
                mBinding.txtMostlyClud.setText(
                    "Mostly Cloudly ${tempKelvinToCelsius(weatherResponse.list[0].main.temp_max)} / ${
                        tempKelvinToCelsius(
                            weatherResponse.list[0].main.temp_min
                        )
                    }"
                )
                mBinding.txtHumidity.setText("Humidity :" +weatherResponse.list[0].main.humidity + " %")
                hourlyAdapter = HourlyForecastAdapter(this, weatherResponse.list)
                mBinding.rvHourlyForecast.adapter = hourlyAdapter
            }

        }
    }

    private fun requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            enableGPS()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun enableGPS() {
        val locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000L).build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            getCurrentLocation()
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, LOCATION_PERMISSION_REQUEST_CODE)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Toast.makeText(
                        this,
                        getString(R.string.unable_to_start_gps_resolution), Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.gps_needs_to_be_enabled),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermissions()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                apiCall(location.latitude.toString(), location.longitude.toString())

            } else {
                requestNewLocationData()
            }
        }.addOnFailureListener {
            Toast.makeText(
                this,
                getString(R.string.failed_to_get_last_known_location), Toast.LENGTH_SHORT
            ).show()
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener(this) { location: Location? ->
                if (location != null) {
                    apiCall(location.latitude.toString(), location.longitude.toString())
                }
            }
    }

    private fun apiCall(latitude: String, longitude: String) {
        // mViewModel.getWeather(latitude.toString(), longitude.toString())
        mViewModel.getHourlyForecast(latitude.toString(), longitude.toString())
    }

    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000L)
            .setMinUpdateIntervalMillis(5000L)
            .setMaxUpdates(1)
            .build()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    apiCall(location.latitude.toString(), location.longitude.toString())

                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Unable to get current location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }, Looper.myLooper())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Please Allow Notification Permission", Toast.LENGTH_LONG)
                    .show()

            }
        }
    }

}