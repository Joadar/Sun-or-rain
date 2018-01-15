package io.smallant.sunorrain.data.models

import java.io.Serializable

data class Weather(
        val coord: Coordinates,
        val weather: List<Detail>,
        val main: Main,
        val dt: Long,
        val sys: Sys,
        val id: Long,
        val name: String,
        var icon: Int
) : Serializable

data class Main(
        var temp: Double,
        val pressure: Double,
        val humidity: Double,
        val temp_min: Double,
        val temp_max: Double
) : Serializable

data class Sys(
        val id: Long,
        val country: String,
        val sunrise: Long,
        val sunset: Long
) : Serializable

data class Coordinates(
        val lon: Double,
        val lat: Double
) : Serializable

data class Detail(
        val id: Long,
        val main: String,
        val description: String,
        val icon: String
) : Serializable

data class Forecast(
        val list: List<ForecastDetail>,
        val city: City
) : Serializable

data class ForecastDetail(
        val dt: Long = 0,
        val temp: Temperature? = null,
        val pressure: Float = 0F,
        val humidity: Int = 0,
        val weather: List<Detail> = listOf(),
        val speed: Float = 0F,
        val deg: Int = 0,
        var icon: Int = 0
) : Serializable

data class Temperature(
        val day: Double,
        val min: Double,
        val max: Double,
        val night: Double,
        val eve: Double,
        val morn: Double,
        val temp: Double,
        val temp_min: Double,
        val temp_max: Double
) : Serializable

data class City(
        val id: Long,
        val name: String,
        val coord: Coordinates,
        val country: String
) : Serializable