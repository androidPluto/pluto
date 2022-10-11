package com.pluto.tools.modules.grid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import com.pluto.tools.R
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.dp2px

internal class GridView(context: Context) : View(context) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var startX = 0
        val gridPaint = object : Paint(ANTI_ALIAS_FLAG) {
            init {
                color = context.color(R.color.pluto___red_40)
                style = Style.FILL
                strokeWidth = 1f.dp2px
            }
        }
        while (startX < measuredWidth) {
            canvas.drawLine(startX.toFloat().dp2px, 0f, startX.toFloat().dp2px, measuredHeight.toFloat(), gridPaint)
            startX += LINE_INTERVAL
        }

        var startY = 0
        while (startY < measuredHeight) {
            canvas.drawLine(0f, startY.toFloat().dp2px, measuredWidth.toFloat(), startY.toFloat().dp2px, gridPaint)
            startY += LINE_INTERVAL
        }
    }

    private companion object {
        const val LINE_INTERVAL = 5
    }
}
