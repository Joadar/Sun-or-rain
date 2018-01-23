package io.smallant.sunorrain.extensions

import io.smallant.sunorrain.R
import java.text.SimpleDateFormat
import java.util.*

val Double?.toCeil: Int
    get() = Math.ceil(this ?: 0.0).toInt()

fun String.checkIcon(description: String, currentCityTime: Long = 0, sunriseTime: Long = 0, sunsetTime: Long = 0, isTwelve: Boolean = false) : Int {

    if(!isTwelve) {
        if (currentCityTime < sunriseTime || currentCityTime >= sunsetTime) {
            return when {
                this.startsWith("01") -> R.drawable.ic_moon
                this.startsWith("02") -> R.drawable.ic_cloudy_night
                else -> R.drawable.ic_clouds
            }
        }
    }

    return when {
        this.startsWith("01") -> R.drawable.ic_sunny
        this.startsWith("02") -> R.drawable.ic_cloudy
        this.startsWith("03") -> R.drawable.ic_clouds
        this.startsWith("04") -> R.drawable.ic_clouds
        this.startsWith("09") -> R.drawable.ic_light_rain
        this.startsWith("10") ->
            when {
                description.startsWith("light") -> R.drawable.ic_light_rain
                description.startsWith("moderate") -> R.drawable.ic_moderate_rain
                else -> R.drawable.ic_heavy_rain
            }
        this.startsWith("11") -> R.drawable.ic_storm
        this.startsWith("13") -> R.drawable.ic_snow
        this.startsWith("50") -> R.drawable.ic_mist
        else -> R.drawable.ic_clouds
    }
}

fun Long.getHoursMinutes(timeZone: String, format: String): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone(timeZone)
    return sdf.format(this * 1000)
}

val Long.toDay: String
    get() {
        val dayNumero = SimpleDateFormat("DD", Locale.getDefault()).format(this * 1000)
        val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(this * 1000)
        return this.checkDay(dayName, dayNumero)
    }

fun Long.checkDay(dayName: String, dayNumero: String): String {
    val epochInMillis = this * 1000
    val now = Calendar.getInstance()
    val timeToCheckToday = Calendar.getInstance()
    timeToCheckToday.timeInMillis = epochInMillis

    val timeToCheckTomorrow = Calendar.getInstance()
    timeToCheckTomorrow.add(Calendar.DAY_OF_YEAR, 1)

    return when (timeToCheckToday.get(Calendar.DAY_OF_YEAR)) {
        now.get(Calendar.DAY_OF_YEAR) -> {
            if (now.get(Calendar.HOUR_OF_DAY) > 19)
                "Tonight"
            else
                "Today"
        }
        timeToCheckTomorrow.get(Calendar.DAY_OF_YEAR) -> "Tomorrow"
        else -> "${dayName.capitalize()}, $dayNumero"
    }
}

// Converts to celcius
fun Double.convertFahrenheitToCelcius() = (this - 32) * 5 / 9

// Converts to fahrenheit
fun Double.convertCelciusToFahrenheit() = this * 9 / 5 + 32