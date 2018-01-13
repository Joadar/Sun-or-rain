package io.smallant.sunorrain.ui.features.nextDays

import io.smallant.sunorrain.data.models.Forecast

interface NextDaysContract {
    interface View {
        fun displayWeekWeather(data: Forecast)
        fun displaySearchError()
    }

    interface Presenter {
        fun getWeekWeather(city: String, refresh: Boolean = true)
        fun getWeekWeather(latitude: Double, longitude: Double, refresh: Boolean = true)
    }
}