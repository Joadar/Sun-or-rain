package io.smallant.sunorrain.data.source

import io.reactivex.Observable
import io.smallant.sunorrain.data.models.Forecast
import io.smallant.sunorrain.data.models.Weather
import io.smallant.sunorrain.extensions.checkIcon
import java.util.*

/**
 * Created by Jonathan on 21/05/2016.
 */

class WeatherRepository(private val remoteDataSource: WeatherDataSource) : WeatherDataSource {

    private var currentWeather: Weather? = null
    private var weekWeather: Forecast? = null
    private var isCurrentWeatherCacheDirty: Boolean = false
    private var isWeekWeatherCacheDirty: Boolean = false

    companion object {
        var INSTANCE: WeatherRepository? = null
        @JvmStatic
        fun getInstance(remoteDataSource: WeatherDataSource): WeatherRepository {
            if (INSTANCE == null) {
                INSTANCE = WeatherRepository(remoteDataSource)
            }

            return INSTANCE as WeatherRepository
        }
    }

    override fun getCurrentWeather(city: String) =
            manageCurrentWeather(remoteDataSource.getCurrentWeather(city))

    override fun getCurrentWeather(latitude: Double, longitude: Double) =
            manageCurrentWeather(remoteDataSource.getCurrentWeather(latitude, longitude))

    override fun getWeekWeather(city: String): Observable<Forecast> =
            manageWeekWeather(remoteDataSource.getWeekWeather(city))

    override fun getWeekWeather(latitude: Double, longitude: Double) =
            manageWeekWeather(remoteDataSource.getWeekWeather(latitude, longitude))

    fun refreshWeatherDay() {
        isCurrentWeatherCacheDirty = true
    }

    fun refreshWeekWeather() {
        isWeekWeatherCacheDirty = true
    }

    private fun manageCurrentWeather(data: Observable<Weather>): Observable<Weather> {
        if (!isCurrentWeatherCacheDirty && currentWeather != null) {
            return Observable.just(currentWeather)
        }

        return data.flatMap {
            Observable.just(it)
        }.doOnNext {
            val date = Date(it.dt * 1000)
            val calendar = Calendar.getInstance(Locale.getDefault())
            calendar.time = date

            currentWeather = it
            currentWeather?.icon = it.weather[0].description.checkIcon(calendar.get(Calendar.HOUR_OF_DAY))
        }.doOnComplete {
            isCurrentWeatherCacheDirty = false
        }
    }

    private fun manageWeekWeather(data: Observable<Forecast>): Observable<Forecast> {
        if (!isWeekWeatherCacheDirty && weekWeather != null) {
            return Observable.just(weekWeather)
        }
        return data.flatMap {
            Observable.just(it)
        }.doOnNext {
            weekWeather = it
            weekWeather?.list?.map {
                it.icon = it.weather[0].description.checkIcon(12)
            }
        }.doOnComplete {
            isWeekWeatherCacheDirty = false
        }
    }
}