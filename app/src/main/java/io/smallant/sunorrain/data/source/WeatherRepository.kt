package io.smallant.sunorrain.data.source

import io.reactivex.Observable
import io.smallant.sunorrain.data.models.Forecast
import io.smallant.sunorrain.data.models.Weather
import io.smallant.sunorrain.extensions.checkIcon

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

    override fun getCurrentWeather(city: String, units: String) =
            manageCurrentWeather(remoteDataSource.getCurrentWeather(city, units))

    override fun getCurrentWeather(latitude: Double, longitude: Double, units: String) =
            manageCurrentWeather(remoteDataSource.getCurrentWeather(latitude, longitude, units))

    override fun getWeekWeather(city: String, units: String): Observable<Forecast> =
            manageWeekWeather(remoteDataSource.getWeekWeather(city, units))

    override fun getWeekWeather(latitude: Double, longitude: Double, units: String) =
            manageWeekWeather(remoteDataSource.getWeekWeather(latitude, longitude, units))

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
            currentWeather = it
            currentWeather?.icon = it.weather[0].description.checkIcon(it.dt, it.sys.sunrise, it.sys.sunset)
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
                it.icon = it.weather[0].description.checkIcon(isTwelve = true)
            }
        }.doOnComplete {
            isWeekWeatherCacheDirty = false
        }
    }
}