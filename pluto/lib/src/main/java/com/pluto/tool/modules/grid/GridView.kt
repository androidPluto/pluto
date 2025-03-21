package com.pluto.tool.modules.grid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import com.pluto.R
import com.pluto.plugin.settings.SettingsPreferences
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.dp2px

/**
 * A custom view that draws a grid pattern on the screen.
 *
 * This view draws horizontal and vertical lines at regular intervals to create a grid overlay.
 * The grid spacing is determined by the SettingsPreferences.gridSize value, and the color
 * of the grid lines adapts based on whether dark theme is enabled.
 *
 * @param context The context used to access resources and settings
 */
internal class GridView(context: Context) : View(context) {
    /**
     * Paint object used to draw the grid lines.
     * The color is determined by the current theme setting (light or dark).
     */
    private val gridPaint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.color(
                if (SettingsPreferences.isDarkThemeEnabled) {
                    com.pluto.plugin.R.color.pluto___red_40
                } else {
                    com.pluto.plugin.R.color.pluto___orange_40
                }
            )
            style = Style.FILL
            strokeWidth = 1f.dp2px
        }
    }

    /**
     * Draws the grid pattern on the canvas.
     *
     * This method draws vertical and horizontal lines at intervals specified by
     * SettingsPreferences.gridSize to create a grid overlay on the screen.
     *
     * @param canvas The canvas on which the grid will be drawn
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw vertical lines
        var startX = 0
        while (startX < measuredWidth) {
            canvas.drawLine(startX.toFloat().dp2px, 0f, startX.toFloat().dp2px, measuredHeight.toFloat(), gridPaint)
            startX += SettingsPreferences.gridSize
        }

        // Draw horizontal lines
        var startY = 0
        while (startY < measuredHeight) {
            canvas.drawLine(0f, startY.toFloat().dp2px, measuredWidth.toFloat(), startY.toFloat().dp2px, gridPaint)
            startY += SettingsPreferences.gridSize
        }
    }
}
