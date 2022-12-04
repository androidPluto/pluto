package com.pluto.plugins.layoutinspector.internal.canvas

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import com.pluto.utilities.extensions.dp2px

internal class GridCanvas(private val container: View) {
    private val paint: Paint = object : Paint() {
        init {
            isAntiAlias = true
            color = -0xaaaaab
            strokeWidth = 1f
        }
    }
    private val measuredWidth: Int
        private get() = container.measuredWidth
    private val measuredHeight: Int
        private get() = container.measuredHeight

    fun draw(canvas: Canvas, alpha: Float) {
        canvas.save()
        var startX = 0
        paint.alpha = (255 * alpha).toInt()
        while (startX < measuredWidth) {
            canvas.drawLine(startX.toFloat(), 0f, startX.toFloat(), measuredHeight.toFloat(), paint)
            startX += LINE_INTERVAL
        }
        var startY = 0
        while (startY < measuredHeight) {
            canvas.drawLine(0f, startY.toFloat(), measuredWidth.toFloat(), startY.toFloat(), paint)
            startY += LINE_INTERVAL
        }
        canvas.restore()
    }

    companion object {
        private val LINE_INTERVAL: Int = 5f.dp2px.toInt()
    }
}
