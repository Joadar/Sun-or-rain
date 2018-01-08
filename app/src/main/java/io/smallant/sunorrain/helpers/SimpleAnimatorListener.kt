package io.smallant.sunorrain.helpers

import android.animation.Animator
import android.support.annotation.Nullable


open class SimpleAnimatorListener() : Animator.AnimatorListener {

    protected var wrapListener: Animator.AnimatorListener? = null

    constructor(@Nullable wrapListener: Animator.AnimatorListener) : this() {
        this.wrapListener = wrapListener
    }

    override fun onAnimationStart(animation: Animator?) {
        if (wrapListener != null)
            wrapListener!!.onAnimationStart(animation)
    }

    override fun onAnimationEnd(animation: Animator?) {
        if (wrapListener != null)
            wrapListener!!.onAnimationEnd(animation)
    }

    override fun onAnimationCancel(animation: Animator?) {
        if (wrapListener != null)
            wrapListener!!.onAnimationCancel(animation)
    }

    override fun onAnimationRepeat(animation: Animator?) {
        if (wrapListener != null)
            wrapListener!!.onAnimationRepeat(animation)
    }
}