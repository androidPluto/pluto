package com.pluto.utilities.extensions

import android.content.Context
import android.view.View
import android.view.WindowManager
import com.pluto.utilities.DebugLog

@SuppressWarnings("TooGenericExceptionCaught")
fun Context.removeViewFromWindow(v: View) {
    try {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.removeView(v)
    } catch (t: Throwable) {
        DebugLog.e("pluto_sdk", "error while removing view", t)
    }
}

@SuppressWarnings("TooGenericExceptionCaught")
fun Context.addViewToWindow(v: View, params: WindowManager.LayoutParams): Boolean {
    return try {
        if (canDrawOverlays()) {
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.addView(v, params)
            true
        } else {
            false
        }
    } catch (t: Throwable) {
        DebugLog.e("pluto_sdk", "error while adding view", t)
        removeViewFromWindow(v)
        false
    }
}
