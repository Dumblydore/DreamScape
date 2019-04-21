package me.mauricee.dreamscape.domain.weather

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("/forecast/{key}/{long}, {lat}")
    fun getForecast(@Path("key") key: String, @Path("long")long: Double, @Path("lat")lat: Double) :Single<Forecast>
}