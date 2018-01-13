package io.smallant.sunorrain.data.source.remote

import io.reactivex.Observable
import io.smallant.sunorrain.data.models.Forecast
import io.smallant.sunorrain.data.models.Weather
import io.smallant.sunorrain.data.services.WeatherService
import io.smallant.sunorrain.data.source.WeatherDataSource
import java.util.*

/**
 * Created by Jonathan on 21/05/2016.
 */

open class RemoteDataSource(private val api: WeatherService = WeatherService()) : WeatherDataSource {

    private val TAG = RemoteDataSource::class.java.simpleName

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

    override fun getCurrentWeather(city: String): Observable<Weather> {
        return api.getCurrentWeather(city)
    }

    override fun getCurrentWeather(latitude: Double, longitude: Double): Observable<Weather> {
        return api.getCurrentWeather(latitude, longitude)
    }

    override fun getWeekWeather(city: String): Observable<Forecast> {
        return api.getWeatherWeek(city)
    }

    override fun getWeekWeather(latitude: Double, longitude: Double): Observable<Forecast> {
        return api.getWeatherWeek(latitude, longitude)
    }

    private fun getLocalHour(timeZone: String): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone))
        return "${calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()).capitalize()}, ${Calendar.HOUR_OF_DAY}:${Calendar.MINUTE}"
    }
}