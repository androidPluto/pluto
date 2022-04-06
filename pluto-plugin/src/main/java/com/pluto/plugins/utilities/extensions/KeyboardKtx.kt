package com.pluto.plugins.utilities.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleCoroutineScope

private const val SMOOTH_TRANSITION_DELAY = 200L

fun ComponentActivity.showKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}

fun ComponentActivity.hideKeyboard(lifecycleScope: LifecycleCoroutineScope? = null, next: (() -> Unit)? = null) {
    if (currentFocus != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
        lifecycleScope?.delayedLaunchWhenResumed(SMOOTH_TRANSITION_DELAY) {
            next?.invoke()
        }
    } else {
        lifecycleScope?.launchWhenResumed {
            next?.invoke()
        }
    }
}

fun View.hideKeyboard(lifecycleScope: LifecycleCoroutineScope? = null, next: (() -> Unit)? = null) {
    if (context != null) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
        lifecycleScope?.delayedLaunchWhenResumed(SMOOTH_TRANSITION_DELAY) {
            next?.invoke()
        }
    } else {
        lifecycleScope?.launchWhenResumed {
            next?.invoke()
        }
    }
}

fun View.showKeyboard() {
    context?.let {
        val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}
