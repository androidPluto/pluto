package com.pluto.tools.modules.grid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.view.ViewConfiguration
import com.pluto.tools.R
import com.pluto.tools.modules.ruler.internal.ScreenMeasurement
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.dp2px
import com.pluto.utilities.extensions.px2dp

internal class GridView(context: Context) : View(context) {

    private val touchSlop: Int
    private var screen = ScreenMeasurement()
    private val gridPaint: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.color(R.color.pluto___red_40)
            style = Style.FILL
            strokeWidth = 1f
        }
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val vc = ViewConfiguration.get(context)
        touchSlop = vc.scaledTouchSlop
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        screen.height = measuredHeight.toFloat().px2dp.toInt()
        screen.width = measuredWidth.toFloat().px2dp.toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var startX = 0
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
