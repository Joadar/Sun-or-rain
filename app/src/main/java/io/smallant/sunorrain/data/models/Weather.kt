package io.smallant.sunorrain.data.models

data class Weather(
        val coord: Coordinates = Coordinates(0.0, 0.0),
        val weather: List<Detail> = arrayListOf(),
        val main: Main = Main(0F, 0F, 0F, 0F, 0F),
        val dt: Long = 0,
        val sys: Sys = Sys(0L, "", 0L, 0L),
        val id: Long = 0,
        val name: String = "",
        var icon: Int = 0
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
        val lon: Double,
        val lat: Double
)

data class Detail(
        val id: Long,
        val main: String,
        val description: String,
        val icon: String
)