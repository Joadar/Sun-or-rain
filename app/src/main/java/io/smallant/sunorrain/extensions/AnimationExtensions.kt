package io.smallant.sunorrain.extensions

import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation

fun View.fadeIn() {
    val fadeIn = AlphaAnimation(0f, 1f)
    fadeIn.interpolator = AccelerateInterpolator()
    fadeIn.duration = 300

    fadeIn.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation) {}

        override fun onAnimationRepeat(animation: Animation) {}
        override fun onAnimationStart(animation: Animation) {}
    })

    this.startAnimation(fadeIn)
}

fun View.fadeOut() {
    val fadeOut = AlphaAnimation(1f, 0f)
    fadeOut.interpolator = AccelerateInterpolator()
    fadeOut.duration = 300

    fadeOut.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation) {
            this@fadeOut.gone()
        }

        override fun onAnimationRepeat(animation: Animation) {}
        override fun onAnimationStart(animation: Animation) {}
    })

    this.startAnimation(fadeOut)
}

fun View.fadeOut(onEnding: () -> Unit) {
    val fadeOut = AlphaAnimation(1f, 0f)
    fadeOut.interpolator = AccelerateInterpolator()
    fadeOut.duration = 300

    fadeOut.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation) {
            this@fadeOut.gone()
            onEnding()
        }

        override fun onAnimationRepeat(animation: Animation) {}
        override fun onAnimationStart(animation: Animation) {}
    })

    this.startAnimation(fadeOut)
}

