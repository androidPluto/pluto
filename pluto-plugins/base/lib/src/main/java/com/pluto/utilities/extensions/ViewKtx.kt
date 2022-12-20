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

fun View.getIdString(): String {
    val out = StringBuilder()
    if (id != View.NO_ID && !isViewIdGenerated(id)) {
        try {
            val pkgName: String = when (id and -0x1000000) {
                0x7f000000 -> "app"
                0x01000000 -> "android"
                else -> this.resources.getResourcePackageName(id)
            }
            val typename: String = this.resources.getResourceTypeName(id)
            val entryName: String = this.resources.getResourceEntryName(id)
            out.append(pkgName)
            out.append(":")
            out.append(typename)
            out.append("/")
            out.append(entryName)
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
            out.append(Integer.toHexString(id))
        }
    } else {
        out.append("NO_ID")
    }
    return out.toString()
}

private fun isViewIdGenerated(id: Int): Boolean {
    return id and -0x1000000 == 0 && id and 0x00FFFFFF != 0
}
