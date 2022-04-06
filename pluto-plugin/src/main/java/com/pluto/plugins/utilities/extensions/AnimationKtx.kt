package com.pluto.plugins.utilities.extensions

import android.view.animation.Animation

inline fun Animation.setListener(
    func: DslAnimationListener.() -> Unit
) {
    val listener = DslAnimationListener()
    listener.func()
    setAnimationListener(listener)
}

class DslAnimationListener : Animation.AnimationListener {

    private var animationRepeatListener: ((animation: Animation?) -> Unit)? = null
    private var animationEndListener: ((animation: Animation?) -> Unit)? = null
    private var animationStartListener: ((animation: Animation?) -> Unit)? = null

    override fun onAnimationRepeat(animation: Animation?) {
        animationRepeatListener?.invoke(animation)
    }

    fun onAnimationRepeat(func: (animation: Animation?) -> Unit) {
        animationRepeatListener = func
    }

    override fun onAnimationEnd(animation: Animation?) {
        animationEndListener?.invoke(animation)
    }

    fun onAnimationEnd(func: (animation: Animation?) -> Unit) {
        animationEndListener = func
    }

    override fun onAnimationStart(animation: Animation?) {
        animationStartListener?.invoke(animation)
    }

    fun onAnimationStart(func: (animation: Animation?) -> Unit) {
        animationStartListener = func
    }
}
