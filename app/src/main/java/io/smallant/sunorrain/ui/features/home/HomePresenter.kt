package io.smallant.sunorrain.ui.features.home

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.smallant.sunorrain.data.source.WeatherRepository

class HomePresenter(private val repository: WeatherRepository) : HomeContract.Presenter {

    var view: HomeContract.View? = null

    override fun getWeather(city: String, refresh: Boolean) {
        if (refresh)
            repository.refreshWeatherDay()

        repository.getCurrentWeather(city)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.displayCurrentWeather(it)
                }, {
                    Log.e("HomePresenterLog", "error on getting current weather = ${it.message}")
                })
    }

    override fun getWeather(latitude: Double, longitude: Double, refresh: Boolean) {
        if (refresh)
            repository.refreshWeatherDay()

        repository.getCurrentWeather(latitude, longitude)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.displayCurrentWeather(it)
                }, {
                    Log.e("HomePresenterLog", "error on getting current weather lat/lon = ${it.message}")
                })
    }
}