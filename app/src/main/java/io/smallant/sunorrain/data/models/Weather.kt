package io.smallant.sunorrain.data.models

data class Weather(
        val coord: Coordinates,
        val weather: List<Detail>,
        val main: Main,
        val dt: Long,
        val sys: Sys,
        val id: Long,
        val name: String,
        var icon: Int
)

data class Main(
        val temp: Double,
        val pressure: Double,
        val humidity: Double,
        val temp_min: Double,
        val temp_max: Double
)

data class Sys(
        val id: Long,
        val country: String,
        val sunrise: Long,
        val sunset: Long
)

data class Coordinates(
        val lon: Double,
        val lat: Double
)

data class Detail(
        val id: Long,
        val main: String,
        val description: String,
        val icon: String
)