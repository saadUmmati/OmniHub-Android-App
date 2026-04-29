package com.example.omnihub.classes

data class WeatherResponse(
    val name: String,
    val main: MainDetails, // Ye temperature wagera ke liye hai
    val weather: List<WeatherDescription>, // Ye condition ke liye hai
    val wind: Wind
)

data class MainDetails(
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val humidity: Int
)

data class WeatherDescription(
    val main: String,   // Ye hai wo "main" jo error de raha tha (e.g., "Rain", "Clouds")
    val description: String,
    val icon: String
)

data class Wind(val speed: Double)