package io.smallant.sunorrain.helpers

import android.animation.Animator
import android.os.Build
import android.view.View
import android.view.ViewAnimationUtils

object CircularRevealCompat {

    fun circularReveal(view: View, centerView: View?, mainLayout: View, endAnimatorListener: Animator.AnimatorListener?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val array = intArrayOf(0, 0)
            centerView?.let {
                it.getLocationOnScreen(array)
                array[0] += it.width / 2
            }

            val cx = array[0]
            val cy = array[1]

            val finalRadius = Math.hypot(mainLayout.width.toDouble(), mainLayout.height.toDouble()).toFloat()
            val animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)

            view.visibility = View.VISIBLE
            endAnimatorListener?.let { animator.addListener(it) }
            animator.duration = 500
            animator.start()
        } else {
            with(view) {
                val animator = android.animation.ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).setDuration(300)
                animator.addListener(object : SimpleAnimatorListener() {
                    override fun onAnimationStart(animation: Animator?) {
                        super.onAnimationStart(animation)
                        view.alpha = 0f
                        view.visibility = android.view.View.VISIBLE
                    }
                })
                endAnimatorListener?.let { animator.addListener(it) }
                animator.start()
            }
        }
    }


    fun circularHide(view: View, centerView: View?, mainLayout: View, endAnimatorListener: Animator.AnimatorListener?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val array = intArrayOf(0, 0)
            centerView?.let {
                it.getLocationOnScreen(array)
                array[0] += it.width / 2
            }

            val cx = array[0]
            val cy = array[1]

            val initialRadius = Math.hypot(mainLayout.width.toDouble(), mainLayout.height.toDouble()).toFloat()
            val animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0f)
            animator.addListener(object : SimpleAnimatorListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.INVISIBLE
                }
            })
            endAnimatorListener?.let { animator.addListener(it) }
            animator.start()
        } else {
            with(view) {
                val animator = android.animation.ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f).setDuration(300)
                animator.addListener(object : SimpleAnimatorListener() {
                    override fun onAnimationStart(animation: Animator?) {
                        super.onAnimationStart(animation)
                        view.alpha = 0f
                        view.visibility = android.view.View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        view.alpha = 0f
                        view.visibility = android.view.View.INVISIBLE
                    }
                })
                endAnimatorListener?.let { animator.addListener(it) }
                animator.start()
            }
        }
    }

}
