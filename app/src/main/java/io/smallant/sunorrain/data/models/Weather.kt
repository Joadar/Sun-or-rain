package io.smallant.sunorrain.data.models

data class Weather(
        val coord: Coordinates,
        val weather: List<Detail>,
        val main: Main,
        val dt: Long,
        val sys: Sys,
        val id: Long,
        val name: String
)

data class Main(
        val temp: Float,
        val pressure: Float,
        val humidity: Float,
        val temp_min: Float,
        val temp_max: Float
)

data class Sys(
        val id: Long,
        val country: String,
        val sunrise: Long,
        val sunset: Long
)

data class Coordinates(
        val lon: Float,
        val lat: Float
)

data class Detail(
        val id: Long,
        val main: String,
        val description: String,
        val icon: String
)