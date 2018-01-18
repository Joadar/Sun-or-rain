package io.smallant.sunorrain.extensions

import io.smallant.sunorrain.R
import java.text.SimpleDateFormat
import java.util.*

val Double?.toCeil: Int
    get() = Math.ceil(this ?: 0.0).toInt()

fun String.checkIcon(currentCityTime: Long = 0, sunriseTime: Long = 0, sunsetTime: Long = 0, isTwelve: Boolean = false) : Int{
        when (this) {
            "light rain" -> return R.drawable.ic_light_rain
            "moderate rain" -> return R.drawable.ic_moderate_rain
            "few clouds" -> return R.drawable.ic_cloudy
            "thunderstorm" -> return R.drawable.ic_storm
            "mist" -> return R.drawable.ic_mist
            else -> {
                when {
                    this.contains("clouds") -> return R.drawable.ic_clouds
                    this.contains("rain") -> return R.drawable.ic_heavy_rain
                    this.contains("snow") -> return R.drawable.ic_snow
                    this == "clear sky" || this == "sky is clear" -> {
                        if (isTwelve)
                            return R.drawable.ic_sunny
                        if (currentCityTime < sunriseTime || currentCityTime >= sunsetTime)
                            return R.drawable.ic_moon
                        else
                            return R.drawable.ic_sunny
                    }
                    else -> return R.drawable.ic_clouds
                }
            }
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