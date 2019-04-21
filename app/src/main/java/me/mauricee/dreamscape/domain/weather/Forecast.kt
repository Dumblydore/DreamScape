package me.mauricee.dreamscape.domain.weather

import com.google.gson.annotations.SerializedName

data class Forecast(@SerializedName("currently") val currently: WeatherPoint)

data class WeatherPoint(@SerializedName("temperature") val temperature: Float)