package com.pluto.plugins.layoutinspector.internal.canvas

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.pluto.plugins.layoutinspector.R
import com.pluto.plugins.layoutinspector.internal.inspect.InspectedView
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.dp2px
import com.pluto.utilities.extensions.px2dp

internal class ClickInfoCanvas(private val container: View) {

    private val cornerRadius = 3f.dp2px
    private val textBgTopPadding = 4f.dp2px
    private val textBgBottomPadding = 1f.dp2px
    private val textBgHorizontalPadding = 4f.dp2px
    private val distanceBtwTextAndBorder = 7f.dp2px

    private val textPaint: Paint = object : Paint() {
        init {
            color = container.context.color(R.color.pluto___red_dark)
            style = Style.FILL
            strokeWidth = 1f.dp2px
            textSize = 12f.dp2px
            typeface = ResourcesCompat.getFont(container.context, R.font.muli_semibold)
            flags = FAKE_BOLD_TEXT_FLAG
        }
    }
    private val textBgPaint: Paint = object : Paint() {
        init {
            isAntiAlias = true
            strokeWidth = 1f.dp2px
            color = container.context.color(R.color.pluto___white)
            style = Style.FILL
        }
    }

    var targetElement: InspectedView? = null

    fun draw(canvas: Canvas, element: InspectedView?) {
        canvas.save()
        element?.let {
            targetElement?.let {
                val rect: Rect = it.rect
                val widthText = "${rect.width().toFloat().px2dp.toInt()} dp"
                drawText(canvas, widthText, rect.centerX() - getTextWidth(textPaint, widthText) / 2, rect.top - distanceBtwTextAndBorder + 3f.dp2px)
                val heightText = "${rect.height().toFloat().px2dp.toInt()} dp"
                drawText(canvas, heightText, rect.right + distanceBtwTextAndBorder, rect.centerY() + getTextHeight(textPaint, heightText) / 2)
            } ?: run {
                container.invalidate()
                return
            }
        }
        canvas.restore()
    }

    private fun drawText(canvas: Canvas, text: String, x: Float, y: Float) {
        var left = x - textBgHorizontalPadding
        var top: Float = y - getTextHeight(textPaint, text) - textBgTopPadding
        var right: Float = x + getTextWidth(textPaint, text) + textBgHorizontalPadding
        var bottom = y + textBgBottomPadding
        // ensure text in screen bound
        if (left < 0) {
            right -= left
            left = 0f
        }
        if (top < 0) {
            bottom -= top
            top = 0f
        }
        if (bottom > canvas.height) {
            val diff = top - bottom
            bottom = canvas.height.toFloat()
            top = bottom + diff
        }
        if (right > canvas.width) {
            val diff = left - right
            right = canvas.width.toFloat()
            left = right + diff
        }
        val tmpRectF = RectF().apply {
            set(left, top, right, bottom)
        }
        canvas.drawRoundRect(tmpRectF, cornerRadius, cornerRadius, textBgPaint)
        canvas.drawText(text, left + textBgTopPadding, bottom - textBgTopPadding, textPaint)
    }

    private fun getTextHeight(paint: Paint, text: String): Float {
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        return rect.height().toFloat()
    }

    private fun getTextWidth(paint: Paint, text: String?): Float {
        return paint.measureText(text)
    }
}
