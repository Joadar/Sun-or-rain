package io.smallant.sunorrain.ui.features.home

import io.smallant.sunorrain.data.models.Weather

interface HomeContract {
    interface View {
        fun displayCurrentWeather(data: Weather)
        fun displaySearchError()
    }

    interface Presenter {
        fun getWeather(city: String, refresh: Boolean = true)
        fun getWeather(latitude: Double, longitude: Double, refresh: Boolean = true)
    }
}