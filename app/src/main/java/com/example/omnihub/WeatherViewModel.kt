package com.example.omnihub

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.omnihub.classes.WeatherState
import androidx.lifecycle.viewModelScope // Fixes viewModelScope
import kotlinx.coroutines.launch           // Fixes .launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _weatherData = MutableLiveData<WeatherState>()
    val weatherData: LiveData<WeatherState> get() = _weatherData

    fun fetchWeather(city: String, forceRefresh: Boolean = false) {
        // Agar data pehle se hai aur hum zabardasti refresh nahi kar rahe, toh return kar jayein
        if (_weatherData.value is WeatherState.Success && !forceRefresh) return
        _weatherData.value = WeatherState.Loading
        viewModelScope.launch {
            val result = repository.getWeatherData(city)
            _weatherData.postValue(result)
        }
    }
}