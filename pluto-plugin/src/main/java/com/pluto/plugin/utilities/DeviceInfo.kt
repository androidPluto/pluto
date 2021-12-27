package com.pluto.plugin.utilities

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

class DeviceInfo(context: Context) {

    private val screenDimension: ScreenDimension by lazy {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        ScreenDimension(size.x, size.y)
    }

    val height = screenDimension.height
    val width = screenDimension.width

    private data class ScreenDimension(val width: Int, val height: Int)
}
