package com.pluto.plugins.layoutinspector.internal.canvas

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import com.pluto.plugins.layoutinspector.R
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.dp2px

internal class GridCanvas(private val container: View) {
    private val gridPaint: Paint = object : Paint() {
        init {
            isAntiAlias = true
            color = container.context.color(R.color.pluto___blue_40)
            strokeWidth = 1f
        }
    }
    private val measuredWidth: Int
        get() = container.measuredWidth
    private val measuredHeight: Int
        get() = container.measuredHeight

    fun draw(canvas: Canvas) {
        canvas.save()
        var startX = 0
        while (startX < measuredWidth) {
            canvas.drawLine(startX.toFloat(), 0f, startX.toFloat(), measuredHeight.toFloat(), gridPaint)
            startX += GRID_DIMEN
        }
        var startY = 0
        while (startY < measuredHeight) {
            canvas.drawLine(0f, startY.toFloat(), measuredWidth.toFloat(), startY.toFloat(), gridPaint)
            startY += GRID_DIMEN
        }
        canvas.restore()
    }

    companion object {
        private val GRID_DIMEN: Int = 5f.dp2px.toInt()
    }
}
