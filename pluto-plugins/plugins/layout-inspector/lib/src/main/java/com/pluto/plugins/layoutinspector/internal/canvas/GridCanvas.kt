package com.pluto.plugins.layoutinspector.internal.canvas

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import com.pluto.plugins.layoutinspector.R
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.dp2px
import com.pluto.utilities.settings.SettingsPreferences

internal class GridCanvas(private val container: View) {
    private val gridPaint: Paint = object : Paint() {
        init {
            color = container.context.color(
                if (SettingsPreferences.isDarkThemeEnabled) {
                    R.color.pluto___red_40
                } else {
                    R.color.pluto___yellow_40
                }
            )
            style = Style.FILL
            strokeWidth = 1f.dp2px
        }
    }

    fun draw(canvas: Canvas) {
        canvas.save()
        var startX = 0
        while (startX < container.measuredWidth) {
            canvas.drawLine(startX.toFloat().dp2px, 0f, startX.toFloat().dp2px, container.measuredHeight.toFloat(), gridPaint)
            startX += SettingsPreferences.gridSize
        }
        var startY = 0
        while (startY < container.measuredHeight) {
            canvas.drawLine(0f, startY.toFloat().dp2px, container.measuredWidth.toFloat(), startY.toFloat().dp2px, gridPaint)
            startY += SettingsPreferences.gridSize
        }
        canvas.restore()
    }
}
