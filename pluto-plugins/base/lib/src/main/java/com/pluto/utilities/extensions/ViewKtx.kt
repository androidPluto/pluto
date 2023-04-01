package com.pluto.utilities.extensions

import android.content.Context
import android.content.res.Resources
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

@SuppressWarnings("MagicNumber")
@Throws(Resources.NotFoundException::class)
fun View.getIdInfo(): ViewIdInfo? {
    val isViewIdGenerated: Boolean = id and -0x1000000 == 0 && id and 0x00FFFFFF != 0
    return if (id != View.NO_ID && !isViewIdGenerated) {
        ViewIdInfo(
            packageName = when (id and -0x1000000) {
                0x7f000000 -> "app"
                0x01000000 -> "android"
                else -> resources.getResourcePackageName(id)
            },
            typeName = resources.getResourceTypeName(id),
            entryName = resources.getResourceEntryName(id)
        )
    } else {
        null
    }
}

data class ViewIdInfo(val packageName: String, val typeName: String, val entryName: String)
