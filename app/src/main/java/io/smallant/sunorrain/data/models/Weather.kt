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

data class Forecast(
        val list: List<ForecastDetail>,
        val city: City
)

data class ForecastDetail(
        val dt: Long = 0,
        val temp: Temperature? = null,
        val pressure: Float = 0F,
        val humidity: Int = 0,
        val weather: List<Detail> = listOf(),
        val speed: Float = 0F,
        val deg: Int = 0
)

data class Temperature(
        val day: Float,
        val min: Float,
        val max: Float,
        val night: Float,
        val eve: Float,
        val morn: Float,
        val temp: Float,
        val temp_min: Float,
        val temp_max: Float
)

data class City(
        val id: Long,
        val name: String,
        val coord: Coordinates,
        val country: String
)