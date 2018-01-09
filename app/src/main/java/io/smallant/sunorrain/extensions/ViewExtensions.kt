package io.smallant.sunorrain.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}

fun WindowManager?.getWindowHeight(): Int {
    val display = this?.defaultDisplay
    val size = Point()
    display?.getSize(size)
    return size.y
}


fun View.visible() {
    if (!this.isVisible())
        visibility = View.VISIBLE
}

fun View.invisible() {
    if (!this.isInvisible())
        visibility = View.INVISIBLE
}

fun View.gone() {
    if (!this.isGone())
        visibility = View.GONE
}

fun View.isVisible() = visibility == View.VISIBLE
fun View.isInvisible() = visibility == View.INVISIBLE
fun View.isGone() = visibility == View.GONE