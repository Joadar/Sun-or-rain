package io.smallant.sunorrain.data.source

import io.reactivex.Single
import io.smallant.sunorrain.data.models.Forecast
import io.smallant.sunorrain.data.models.Weather


/**
 * Created by Jonathan on 21/05/2016.
 */

interface WeatherDataSource {
    fun getCurrentWeather(city: String, units: String): Single<Weather>
    fun getCurrentWeather(latitude: Double, longitude: Double, units: String): Single<Weather>
    fun getWeekWeather(city: String, units: String): Single<Forecast>
    fun getWeekWeather(latitude: Double, longitude: Double, units: String): Single<Forecast>
}