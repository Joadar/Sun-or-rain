package io.smallant.sunorrain.data.source.remote

import io.reactivex.Observable
import io.smallant.sunorrain.data.models.Forecast
import io.smallant.sunorrain.data.models.Weather
import io.smallant.sunorrain.data.services.WeatherService
import io.smallant.sunorrain.data.source.WeatherDataSource

/**
 * Created by Jonathan on 21/05/2016.
 */

open class RemoteDataSource(private val api: WeatherService = WeatherService()) : WeatherDataSource {
    
    companion object {
        var INSTANCE: RemoteDataSource? = null

        @JvmStatic
        fun getInstance(): RemoteDataSource {
            if (INSTANCE == null) {
                INSTANCE = RemoteDataSource(WeatherService())
            }

            return INSTANCE as RemoteDataSource
        }
    }

    override fun getCurrentWeather(city: String, units: String): Observable<Weather> {
        return api.getCurrentWeather(city, units)
    }

    override fun getCurrentWeather(latitude: Double, longitude: Double, units: String): Observable<Weather> {
        return api.getCurrentWeather(latitude, longitude, units)
    }

    override fun getWeekWeather(city: String, units: String): Observable<Forecast> {
        return api.getWeatherWeek(city, units)
    }

    override fun getWeekWeather(latitude: Double, longitude: Double, units: String): Observable<Forecast> {
        return api.getWeatherWeek(latitude, longitude, units)
    }
}