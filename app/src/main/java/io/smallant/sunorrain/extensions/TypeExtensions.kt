package io.smallant.sunorrain.extensions

import android.content.res.Resources

val Int.toPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.toDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Double?.toCeil: Int
    get() = Math.ceil(this ?: 0.0).toInt()