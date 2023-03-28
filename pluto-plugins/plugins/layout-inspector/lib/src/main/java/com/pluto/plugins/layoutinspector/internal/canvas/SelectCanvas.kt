package com.pluto.plugins.layoutinspector.internal.canvas

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import com.pluto.plugins.layoutinspector.internal.inspect.Element
import com.pluto.utilities.extensions.dp2px

internal class SelectCanvas(private val view: View) {

    private val cornerPaint: Paint = object : Paint() {
        init {
            isAntiAlias = true
            strokeWidth = 1f.dp2px
        }
    }
    private val areaPaint: Paint = object : Paint() {
        init {
            isAntiAlias = true
            color = Color.BLUE
            style = Style.STROKE
            strokeWidth = 1f.dp2px
        }
    }
    private val dashLinePaint: Paint = object : Paint() {
        init {
            isAntiAlias = true
            color = Color.GREEN
            style = Style.STROKE
            pathEffect = DashPathEffect(floatArrayOf(3f.dp2px, 3f.dp2px), 0f)
        }
    }
    private val cornerRadius: Float = 1.5f.dp2px

    private val measuredHeight: Float
        get() = view.measuredHeight.toFloat()

    private val measuredWidth: Float
        get() = view.measuredWidth.toFloat()

    init {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    fun draw(canvas: Canvas, vararg elements: Element?) {
        canvas.save()
        for (element in elements) {
            element?.let { drawSelected(canvas, it) }
        }
        canvas.restore()
    }

    private fun drawSelected(canvas: Canvas, element: Element) {
        val rect: Rect = element.rect
        canvas.drawLine(0f, rect.top.toFloat(), measuredWidth, rect.top.toFloat(), dashLinePaint)
        canvas.drawLine(0f, rect.bottom.toFloat(), measuredWidth, rect.bottom.toFloat(), dashLinePaint)
        canvas.drawLine(rect.left.toFloat(), 0f, rect.left.toFloat(), measuredHeight, dashLinePaint)
        canvas.drawLine(rect.right.toFloat(), 0f, rect.right.toFloat(), measuredHeight, dashLinePaint)
        canvas.drawRect(rect, areaPaint)
        cornerPaint.color = Color.WHITE
        cornerPaint.style = Paint.Style.FILL
        canvas.drawCircle(rect.left.toFloat(), rect.top.toFloat(), cornerRadius, cornerPaint)
        canvas.drawCircle(rect.right.toFloat(), rect.top.toFloat(), cornerRadius, cornerPaint)
        canvas.drawCircle(rect.left.toFloat(), rect.bottom.toFloat(), cornerRadius, cornerPaint)
        canvas.drawCircle(rect.right.toFloat(), rect.bottom.toFloat(), cornerRadius, cornerPaint)
        cornerPaint.color = Color.RED
        cornerPaint.style = Paint.Style.STROKE
        canvas.drawCircle(rect.left.toFloat(), rect.top.toFloat(), cornerRadius, cornerPaint)
        canvas.drawCircle(rect.right.toFloat(), rect.top.toFloat(), cornerRadius, cornerPaint)
        canvas.drawCircle(rect.left.toFloat(), rect.bottom.toFloat(), cornerRadius, cornerPaint)
        canvas.drawCircle(rect.right.toFloat(), rect.bottom.toFloat(), cornerRadius, cornerPaint)
    }
}
