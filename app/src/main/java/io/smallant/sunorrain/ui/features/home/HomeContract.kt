package io.smallant.sunorrain.ui.features.home

import io.smallant.sunorrain.data.models.Weather

interface HomeContract {
    interface View {
        fun displayCurrentWeather(data: Weather)
    }

    interface Presenter {
        fun getWeather(city: String)
        fun getWeather(latitude: Double, longitude: Double)
    }
}