package io.smallant.sunorrain.extensions

import android.content.res.Resources
import io.smallant.sunorrain.R

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

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