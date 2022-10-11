package com.pluto.tools.modules.grid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import com.pluto.tools.R
import com.pluto.utilities.extensions.addViewToWindow
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.dp2px
import com.pluto.utilities.extensions.removeViewFromWindow

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

    fun toggle() {
        if (isShowing()) {
            hideGrid()
        } else {
            showGrid()
        }
    }

    private fun showGrid() {
        val params = WindowManager.LayoutParams()
        params.width = FrameLayout.LayoutParams.MATCH_PARENT
        params.height = FrameLayout.LayoutParams.MATCH_PARENT
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        } else {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        params.format = PixelFormat.TRANSLUCENT
        context.addViewToWindow(this, params)
    }

    internal fun hideGrid() {
        context.removeViewFromWindow(this)
    }

    private fun isShowing() = ViewCompat.isAttachedToWindow(this)

    private companion object {
        const val LINE_INTERVAL = 5
    }
}
