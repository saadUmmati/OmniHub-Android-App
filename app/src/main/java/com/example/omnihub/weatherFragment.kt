package com.example.omnihub

import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.omnihub.classes.WeatherResponse
import com.example.omnihub.classes.WeatherState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.Manifest
import android.content.pm.PackageManager
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class WeatherFragment : Fragment(R.layout.fragment_weather) {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    // 1. Permission request ko class level par define karein
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)) {
            getCurrentLocation()
        } else {
            // User ne permission deny ki, toh fallback
            viewModel.fetchWeather("Islamabad")
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        // Date Set karna
        setCurrentDate(view)

        // Location Check
        // Manual Dependency Injection
        val repository = WeatherRepository(RetrofitClient.weatherApi)
        val factory = WeatherViewModelFactory(repository)
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshWeather)
        viewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]




        swipeRefresh.setOnRefreshListener {
            // Current city fetch karke force refresh karein
            val currentCity = view.findViewById<TextView>(R.id.tvLocation).text.toString()
            viewModel.fetchWeather(currentCity, forceRefresh = true)
        }

        // Observer mein loading khatam hone par arrow rok dein
        viewModel.weatherData.observe(viewLifecycleOwner) { state ->
            if (state !is WeatherState.Loading) {
                swipeRefresh.isRefreshing = false
            }
            // ... baki states handle karein ...
            when (state) {
                is WeatherState.Loading -> { /* Show Loader */ }
                is WeatherState.Success -> updateUI(state.data, view)
                is WeatherState.Error -> Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
        }


        // 2. Permission maangein (Ye khud hi getCurrentLocation() call karega agar grant hui)
        checkAndRequestLocation()

    }



    private fun checkAndRequestLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Priority high rakhein taake foran result mile
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val cityName = getCityName(location.latitude, location.longitude)
                    viewModel.fetchWeather(cityName)
                } else {
                    // Agar lastLocation null hai (kabhi kabhi GPS off hone par hota hai)
                    viewModel.fetchWeather("Islamabad")
                }
            }
        }
    }

    private fun getCityName(lat: Double, lon: Double): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        return addresses?.get(0)?.locality ?: "Islamabad" // Fallback city
    }

    private fun setCurrentDate(view: View) {
        val dateText = view.findViewById<TextView>(R.id.tvDate) // XML mein date wali ID check karein
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())
        dateText.text = currentDate
    }

    private fun updateUI(weather: WeatherResponse, view: View) {

        val tempMain = view.findViewById<TextView>(R.id.tvMainTemp)
        val location = view.findViewById<TextView>(R.id.tvLocation)
        val details = view.findViewById<TextView>(R.id.tvDetails)

        //updating main image according to weather condition
        val ivMainWeather = view.findViewById<ImageView>(R.id.ivMainWeather)
        val weatherCondition = weather.weather[0].main // e.g., "Clear", "Clouds", "Rain"
        val iconCode = weather.weather[0].icon // e.g., "01d"
        // Logic for dynamic images
        when {
            weatherCondition.contains("Clear", ignoreCase = true) -> {
                ivMainWeather.setImageResource(R.drawable.sun) // Aapke drawable ka naam
            }
            weatherCondition.contains("Cloud", ignoreCase = true) -> {
                ivMainWeather.setImageResource(R.drawable.ic_cloud)
            }
            weatherCondition.contains("Rain", ignoreCase = true) -> {
                ivMainWeather.setImageResource(R.drawable.weather_rain_sun)
            }
            weatherCondition.contains("Snow", ignoreCase = true) -> {
            }
            else -> {
                ivMainWeather.setImageResource(R.drawable.baseline_wb_sunny_24)
            }
        }

        // Stats bar IDs (Apne XML mein IDs check kar lein)
        val humidityText = view.findViewById<TextView>(R.id.tvHumidity) // XML mein ID add karein
        val windText = view.findViewById<TextView>(R.id.tvWind)         // XML mein ID add karein

        location.text = weather.name
        tempMain.text = "${weather.main.temp.toInt()}°"
        details.text = "${weather.weather[0].description}\nMax: ${weather.main.temp_max.toInt()}° Min: ${weather.main.temp_min.toInt()}°"

        // Example updates for stats
        // humidityText.text = "${weather.main.humidity}%"
        // windText.text = "${weather.wind.speed} km/h"
    }
}