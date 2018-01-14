package io.smallant.sunorrain.ui.features.nextDays

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.smallant.sunorrain.data.source.WeatherRepository

class NextDaysPresenter(private val repository: WeatherRepository) : NextDaysContract.Presenter {

    var view: NextDaysContract.View? = null

    override fun getWeekWeather(city: String, refresh: Boolean) {
        if (refresh)
            repository.refreshWeekWeather()

        repository.getWeekWeather(city)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.displayWeekWeather(it)
                }, {
                    Log.e("HomePresenterLog", "error on getting current weather lat/lon = ${it.message}")
                    view?.displaySearchError()
                })
    }

    override fun getWeekWeather(latitude: Double, longitude: Double, refresh: Boolean) {
        if (refresh)
            repository.refreshWeekWeather()

        repository.getWeekWeather(latitude, longitude)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.displayWeekWeather(it)
                }, {
                    Log.e("HomePresenterLog", "error on getting current weather lat/lon = ${it.message}")
                    view?.displaySearchError()
                })
    }
}