package com.example.omnihub

import com.example.omnihub.classes.WeatherResponse
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query

// API Interface
interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<WeatherResponse>
}