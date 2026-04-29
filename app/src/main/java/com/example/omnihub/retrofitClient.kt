package com.example.omnihub

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val WEATHER_URL = "https://api.openweathermap.org/"
    private const val NEWS_URL = "https://newsapi.org/"

    // News API Service (Pichla wala)
    val newsApi: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(NEWS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }

    // Weather API Service
    val weatherApi: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}