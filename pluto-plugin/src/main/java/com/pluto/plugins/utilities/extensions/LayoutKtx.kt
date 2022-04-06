package com.pluto.plugins.utilities.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope

fun ViewGroup.inflate(layoutResId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutResId, this, attachToRoot)

fun Context.inflate(layoutResId: Int): View = LayoutInflater.from(this).inflate(layoutResId, null)

fun View.fadeInAndOut(lifecycleScope: LifecycleCoroutineScope) {
    val fadeIn = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1f)
    fadeIn.duration = SMOOTH_TRANSITION_DELAY
    val fadeOut = ObjectAnimator.ofFloat(this, "alpha", 1f, 0.0f)
    fadeOut.duration = SMOOTH_TRANSITION_DELAY

    fadeIn.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            visibility = View.VISIBLE
        }

        override fun onAnimationEnd(animation: Animator?) {
            lifecycleScope.delayedLaunchWhenResumed(FADE_IN_OUT_ANIMATION_DURATION) {
                fadeOut.start()
            }
        }
    })
    fadeOut.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            visibility = View.GONE
        }
    })
    fadeIn.start()
}

private const val SMOOTH_TRANSITION_DELAY = 200L
private const val FADE_IN_OUT_ANIMATION_DURATION = 3000L
