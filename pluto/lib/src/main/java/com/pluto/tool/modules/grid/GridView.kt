package com.pluto.tool.modules.grid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import com.pluto.R
import com.pluto.plugin.settings.SettingsPreferences
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.dp2px

internal class GridView(context: Context) : View(context) {
    private val gridPaint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.color(
                if (SettingsPreferences.isDarkThemeEnabled) {
                    R.color.pluto___red_40
                } else {
                    R.color.pluto___orange_40
                }
            )
            style = Style.FILL
            strokeWidth = 1f.dp2px
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var startX = 0
        while (startX < measuredWidth) {
            canvas.drawLine(startX.toFloat().dp2px, 0f, startX.toFloat().dp2px, measuredHeight.toFloat(), gridPaint)
            startX += SettingsPreferences.gridSize
        }

        var startY = 0
        while (startY < measuredHeight) {
            canvas.drawLine(0f, startY.toFloat().dp2px, measuredWidth.toFloat(), startY.toFloat().dp2px, gridPaint)
            startY += SettingsPreferences.gridSize
        }
    }
}
