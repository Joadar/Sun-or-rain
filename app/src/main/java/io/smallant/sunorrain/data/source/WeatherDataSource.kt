package io.smallant.sunorrain.data.source

import io.reactivex.Observable
import io.smallant.sunorrain.data.models.Weather


/**
 * Created by Jonathan on 21/05/2016.
 */

interface WeatherDataSource {
    fun getWeekWeather(city: String): Observable<Weather>
    fun getCurrentWeather(city: String): Observable<Weather>
    fun getCurrentWeather(latitude: Double, longitude: Double): Observable<Weather>
    fun getWeekWeather(latitude: Double, longitude: Double): Observable<Weather>
}