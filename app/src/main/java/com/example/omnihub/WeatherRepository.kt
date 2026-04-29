package com.example.omnihub

import com.example.omnihub.classes.WeatherState
import com.google.firebase.appdistribution.gradle.ApiService

// Repository
class WeatherRepository(private val apiService: WeatherApiService) {
    suspend fun getWeatherData(city: String): WeatherState {
        return try {
            val response = apiService.getWeather(city, "69950d7a0721c1b9a1a64db99a43f92d")
            if (response.isSuccessful && response.body() != null) {
                WeatherState.Success(response.body()!!)
            } else {
                WeatherState.Error("City not found!")
            }
        } catch (e: Exception) {
            WeatherState.Error("Network Error: ${e.message}")
        }
    }
}