package io.smallant.sunorrain.data.source

import io.reactivex.Single
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

    override fun getWeekWeather(city: String, units: String): Single<Forecast> =
            manageWeekWeather(remoteDataSource.getWeekWeather(city, units))

    override fun getWeekWeather(latitude: Double, longitude: Double, units: String) =
            manageWeekWeather(remoteDataSource.getWeekWeather(latitude, longitude, units))

    fun refreshWeatherDay() {
        isCurrentWeatherCacheDirty = true
    }

    fun refreshWeekWeather() {
        isWeekWeatherCacheDirty = true
    }

    private fun manageCurrentWeather(data: Single<Weather>): Single<Weather> {
        if (!isCurrentWeatherCacheDirty && currentWeather != null) {
            return Single.just(currentWeather)
        }

        return data.flatMap {
            Single.just(it)
        }.doOnSuccess {
            currentWeather = it
            currentWeather?.icon = it.weather[0].icon.checkIcon(it.weather[0].description, it.dt, it.sys.sunrise, it.sys.sunset)
            isCurrentWeatherCacheDirty = false
        }
    }

    private fun manageWeekWeather(data: Single<Forecast>): Single<Forecast> {
        if (!isWeekWeatherCacheDirty && weekWeather != null) {
            return Single.just(weekWeather)
        }
        return data.flatMap {
            Single.just(it)
        }.doOnSuccess {
            weekWeather = it
            weekWeather?.list?.map {
                it.icon = it.weather[0].icon.checkIcon(it.weather[0].description, isTwelve = true)
            }
            isWeekWeatherCacheDirty = false
        }
    }
}