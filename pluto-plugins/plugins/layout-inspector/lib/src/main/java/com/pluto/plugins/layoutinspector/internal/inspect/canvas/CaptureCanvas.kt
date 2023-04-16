package com.pluto.plugins.layoutinspector.internal.inspect.canvas

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import com.pluto.plugins.layoutinspector.R
import com.pluto.plugins.layoutinspector.internal.inspect.InspectedView
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.dp2px
import com.pluto.utilities.settings.SettingsPreferences

internal class CaptureCanvas(private val container: View) {

    private val cornerCirclePaint: Paint = object : Paint() {
        init {
            isAntiAlias = true
            strokeWidth = 1f.dp2px
        }
    }
    private val captureBoxPaint: Paint = object : Paint() {
        init {
            isAntiAlias = true
            color = container.context.color(R.color.pluto___blue)
            style = Style.STROKE
            strokeWidth = 1f.dp2px
        }
    }
    private val dashLinePaint: Paint = object : Paint() {
        init {
            isAntiAlias = true
            color = container.context.color(R.color.pluto___emerald)
            style = Style.STROKE
            strokeWidth = 1f
            pathEffect = DashPathEffect(floatArrayOf(3f.dp2px, 3f.dp2px), 0f)
        }
    }
    private val cornerRadius: Float = 1f.dp2px

    private val measuredHeight: Float
        get() = container.measuredHeight.toFloat()

    private val measuredWidth: Float
        get() = container.measuredWidth.toFloat()

    init {
        container.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    fun draw(canvas: Canvas, inspectedView: InspectedView?) {
        canvas.save()
        inspectedView?.let { drawSelected(canvas, it) }
        canvas.restore()
    }

    private fun drawSelected(canvas: Canvas, inspectedView: InspectedView) {
        val rect: Rect = inspectedView.rect
        canvas.drawLine(0f, rect.top.toFloat(), measuredWidth, rect.top.toFloat(), dashLinePaint)
        canvas.drawLine(0f, rect.bottom.toFloat(), measuredWidth, rect.bottom.toFloat(), dashLinePaint)
        canvas.drawLine(rect.left.toFloat(), 0f, rect.left.toFloat(), measuredHeight, dashLinePaint)
        canvas.drawLine(rect.right.toFloat(), 0f, rect.right.toFloat(), measuredHeight, dashLinePaint)
        canvas.drawRect(rect, captureBoxPaint)
        cornerCirclePaint.color = container.context.color(R.color.pluto___transparent)
        cornerCirclePaint.style = Paint.Style.FILL
        canvas.drawCircle(rect.left.toFloat(), rect.top.toFloat(), cornerRadius, cornerCirclePaint)
        canvas.drawCircle(rect.right.toFloat(), rect.top.toFloat(), cornerRadius, cornerCirclePaint)
        canvas.drawCircle(rect.left.toFloat(), rect.bottom.toFloat(), cornerRadius, cornerCirclePaint)
        canvas.drawCircle(rect.right.toFloat(), rect.bottom.toFloat(), cornerRadius, cornerCirclePaint)
        cornerCirclePaint.color = container.context.color(
            if (SettingsPreferences.isDarkThemeEnabled) {
                R.color.pluto___red
            } else {
                R.color.pluto___yellow
            }
        )
        cornerCirclePaint.style = Paint.Style.STROKE
        canvas.drawCircle(rect.left.toFloat(), rect.top.toFloat(), cornerRadius, cornerCirclePaint)
        canvas.drawCircle(rect.right.toFloat(), rect.top.toFloat(), cornerRadius, cornerCirclePaint)
        canvas.drawCircle(rect.left.toFloat(), rect.bottom.toFloat(), cornerRadius, cornerCirclePaint)
        canvas.drawCircle(rect.right.toFloat(), rect.bottom.toFloat(), cornerRadius, cornerCirclePaint)
    }
}
