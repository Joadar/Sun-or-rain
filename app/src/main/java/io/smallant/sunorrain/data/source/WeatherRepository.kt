package io.smallant.sunorrain.data.source

import io.reactivex.Observable
import io.smallant.sunorrain.R
import io.smallant.sunorrain.data.models.Weather
import java.util.*

/**
 * Created by Jonathan on 21/05/2016.
 */

class WeatherRepository(private val remoteDataSource: WeatherDataSource) : WeatherDataSource {

    var cityWeather: Weather? = null
    var isCacheDirty: Boolean = false

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

    override fun getWeekWeather(city: String): Observable<Weather> {
        if (!isCacheDirty && cityWeather != null) {
            return Observable.just(cityWeather)
        }

        val remoteData = remoteDataSource.getWeekWeather(city)
                .flatMap {
                    Observable.just(it)
                }
                .doOnNext {
                    cityWeather = it
                }
                .doOnComplete {
                    isCacheDirty = false
                }

        return remoteData
    }

    override fun getCurrentWeather(city: String): Observable<Weather> {
        if (!isCacheDirty && cityWeather != null) {
            return Observable.just(cityWeather)
        }

        val remoteData = remoteDataSource.getCurrentWeather(city)
                .flatMap {
                    Observable.just(it)
                }
                .doOnNext {
                    val date = Date(it.dt * 1000)
                    val calendar = Calendar.getInstance(Locale.getDefault())
                    calendar.time = date

                    cityWeather = it
                    cityWeather?.icon = checkIcon(it.weather[0].description, calendar.get(Calendar.HOUR_OF_DAY))
                }
                .doOnComplete {
                    isCacheDirty = false
                }

        return remoteData
    }

    override fun getCurrentWeather(latitude: Double, longitude: Double): Observable<Weather> {
        if (!isCacheDirty && cityWeather != null) {
            return Observable.just(cityWeather)
        }

        return remoteDataSource.getCurrentWeather(latitude, longitude)
                .doOnNext {
                    val date = Date(it.dt * 1000)
                    val calendar = Calendar.getInstance(Locale.getDefault())
                    calendar.time = date

                    cityWeather = it
                    cityWeather?.icon = checkIcon(it.weather[0].description, calendar.get(Calendar.HOUR_OF_DAY))
                }
                .doOnComplete {
                    isCacheDirty = false
                }
    }

    override fun getWeekWeather(latitude: Double, longitude: Double): Observable<Weather> {
        if (!isCacheDirty && cityWeather != null) {
            return Observable.just(cityWeather)
        }

        val remoteData = remoteDataSource.getWeekWeather(latitude, longitude)
                .flatMap {
                    Observable.just(it)
                }
                .doOnNext {
                    cityWeather = it
                }
                .doOnComplete {
                    isCacheDirty = false
                }

        return remoteData
    }

    fun refreshWeatherDay() {
        isCacheDirty = true
    }

    private fun checkIcon(name: String, hour: Int): Int {
        when (name) {
            "light rain" -> return R.drawable.light_rain
            "moderate rain" -> return R.drawable.moderate_rain
            "few clouds" -> return R.drawable.cloudly
            "thunderstorm" -> return R.drawable.storm
            "mist" -> return R.drawable.fog_day
            else -> {
                if (name.contains("clouds")) return R.drawable.clouds
                else if (name.contains("rain")) return R.drawable.rain
                else if (name.contains("snow")) return R.drawable.snow
                else if (name.equals("clear sky")) {
                    if (hour >= 18 || hour < 7) {
                        return R.drawable.moon
                    } else {
                        R.drawable.sun
                    }
                }
                return R.drawable.sun
            }
        }
    }
}