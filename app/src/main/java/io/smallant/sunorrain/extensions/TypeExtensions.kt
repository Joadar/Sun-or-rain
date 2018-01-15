package io.smallant.sunorrain.extensions

import io.smallant.sunorrain.R
import java.text.SimpleDateFormat
import java.util.*

val Double?.toCeil: Int
    get() = Math.ceil(this ?: 0.0).toInt()

fun String.checkIcon(hour: Int): Int {
    when (this) {
        "light rain" -> return R.drawable.light_rain
        "moderate rain" -> return R.drawable.moderate_rain
        "few clouds" -> return R.drawable.cloudly
        "thunderstorm" -> return R.drawable.storm
        "mist" -> return R.drawable.fog_day
        else -> {
            if (this.contains("clouds")) return R.drawable.clouds
            else if (this.contains("rain")) return R.drawable.rain
            else if (this.contains("snow")) return R.drawable.snow
            else if (this == "clear sky") {
                if (hour >= 18 || hour < 7) {
                    return R.drawable.moon
                } else {
                    R.drawable.sun
                }
            }
            return R.drawable.sun
        }
    }
}

fun Long.getHoursMinutes(timeZone: String) =
        String.format(
                "%02d:%02d",
                this.calendarFromTimeZone(timeZone).get(Calendar.HOUR_OF_DAY),
                this.calendarFromTimeZone(timeZone).get(Calendar.MINUTE)
        )

fun Long.calendarFromTimeZone(timeZone: String): Calendar {
    val date = Date(this * 1000)
    val calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone), Locale.getDefault())
    calendar.time = date
    return calendar
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
fun Double.convertCelciusToFahrenheit() = this* 9 / 5 + 32