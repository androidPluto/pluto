package com.pluto.plugins.layoutinspector.internal.canvas

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import com.pluto.plugins.layoutinspector.internal.Element
import com.pluto.utilities.extensions.dp2px
import com.pluto.utilities.extensions.px2dp
import kotlin.math.roundToInt

internal class RelativeCanvas(private val container: View) {
    private val cornerRadius = 1.5f.dp2px
    private val endPointSpace = 2f.dp2px
    private val textBgFillingSpace = 3f.dp2px
    private val textLineDistance = 6f.dp2px
    private val areaPaint: Paint = object : Paint() {
        init {
            isAntiAlias = true
            color = Color.RED
            style = Style.STROKE
            strokeWidth = 1f.dp2px
        }
    }
    private val textPaint: Paint = object : Paint() {
        init {
            isAntiAlias = true
            textSize = 10f.dp2px
            color = Color.RED
            style = Style.FILL
            strokeWidth = 1f.dp2px
            flags = FAKE_BOLD_TEXT_FLAG
        }
    }
    private val cornerPaint: Paint = object : Paint() {
        init {
            isAntiAlias = true
            strokeWidth = 1f.dp2px
        }
    }
    private val tmpRectF: RectF = RectF()
    private val measuredWidth: Int
        get() = container.measuredWidth
    private val measuredHeight: Int
        get() = container.measuredHeight

    fun draw(canvas: Canvas, element1: Element?, element2: Element?) {
        if (element1 != null && element2 != null) {
            canvas.save()
            val firstRect: Rect = element1.rect
            val secondRect: Rect = element2.rect
            if (secondRect.top > firstRect.bottom) {
                val x = secondRect.left + secondRect.width() / 2
                drawLineWithText(canvas, x, firstRect.bottom, x, secondRect.top)
            }
            if (firstRect.top > secondRect.bottom) {
                val x = secondRect.left + secondRect.width() / 2
                drawLineWithText(canvas, x, secondRect.bottom, x, firstRect.top)
            }
            if (secondRect.left > firstRect.right) {
                val y = secondRect.top + secondRect.height() / 2
                drawLineWithText(canvas, secondRect.left, y, firstRect.right, y)
            }
            if (firstRect.left > secondRect.right) {
                val y = secondRect.top + secondRect.height() / 2
                drawLineWithText(canvas, secondRect.right, y, firstRect.left, y)
            }
            drawNestedAreaLine(canvas, firstRect, secondRect)
            drawNestedAreaLine(canvas, secondRect, firstRect)
            canvas.restore()
        }
    }

    private fun drawLineWithText(canvas: Canvas, _startX: Int, _startY: Int, _endX: Int, _endY: Int) {
        var startX = _startX
        var startY = _startY
        var endX = _endX
        var endY = _endY
        if (startX == endX && startY == endY) {
            return
        }
        if (startX > endX) {
            val tempX = startX
            startX = endX
            endX = tempX
        }
        if (startY > endY) {
            val tempY = startY
            startY = endY
            endY = tempY
        }
        if (startX == endX) {
            drawLineWithEndPoint(canvas, startX, (startY + endPointSpace).roundToInt(), endX, (endY - endPointSpace).roundToInt())
            val text = "${(endY - startY).toFloat().px2dp} dp"
            drawText(canvas, text, (startX + textLineDistance), startY + (endY - startY) / 2 + getTextHeight(textPaint, text) / 2)
        } else if (startY == endY) {
            drawLineWithEndPoint(canvas, (startX + endPointSpace).roundToInt(), startY, (endX - endPointSpace).roundToInt(), endY)
            val text = "${(endX - startX).toFloat().px2dp} dp"
            drawText(canvas, text, startX + (endX - startX) / 2 - getTextWidth(textPaint, text) / 2, (startY - textLineDistance))
        }
    }

    private fun drawLineWithEndPoint(canvas: Canvas, startX: Int, startY: Int, endX: Int, endY: Int) {
        canvas.drawLine(startX.toFloat(), startY.toFloat(), endX.toFloat(), endY.toFloat(), areaPaint)
        if (startX == endX) {
            canvas.drawLine((startX - endPointSpace), startY.toFloat(), (endX + endPointSpace), startY.toFloat(), areaPaint)
            canvas.drawLine((startX - endPointSpace), endY.toFloat(), (endX + endPointSpace), endY.toFloat(), areaPaint)
        } else if (startY == endY) {
            canvas.drawLine(startX.toFloat(), (startY - endPointSpace), startX.toFloat(), (endY + endPointSpace), areaPaint)
            canvas.drawLine(endX.toFloat(), (startY - endPointSpace), endX.toFloat(), (endY + endPointSpace), areaPaint)
        }
    }

    private fun drawText(canvas: Canvas, text: String, x: Float, y: Float) {
        var left = x - textBgFillingSpace
        var top: Float = y - getTextHeight(textPaint, text)
        var right: Float = x + getTextWidth(textPaint, text) + textBgFillingSpace
        var bottom = y + textBgFillingSpace
        // ensure text in screen bound
        if (left < 0) {
            right -= left
            left = 0f
        }
        if (top < 0) {
            bottom -= top
            top = 0f
        }
        if (bottom > measuredHeight) {
            val diff = top - bottom
            bottom = measuredHeight.toFloat()
            top = bottom + diff
        }
        if (right > measuredWidth) {
            val diff = left - right
            right = measuredWidth.toFloat()
            left = right + diff
        }
        cornerPaint.color = Color.WHITE
        cornerPaint.style = Paint.Style.FILL
        tmpRectF.set(left, top, right, bottom)
        canvas.drawRoundRect(tmpRectF, cornerRadius, cornerRadius, cornerPaint)
        canvas.drawText(text, left + textBgFillingSpace, bottom - textBgFillingSpace, textPaint)
    }

    private fun drawNestedAreaLine(canvas: Canvas, firstRect: Rect, secondRect: Rect) {
        if (secondRect.left >= firstRect.left && secondRect.right <= firstRect.right && secondRect.top >= firstRect.top && secondRect.bottom <= firstRect.bottom) {
            drawLineWithText(
                canvas, secondRect.left, secondRect.top + secondRect.height() / 2,
                firstRect.left, secondRect.top + secondRect.height() / 2
            )
            drawLineWithText(
                canvas, secondRect.right, secondRect.top + secondRect.height() / 2,
                firstRect.right, secondRect.top + secondRect.height() / 2
            )
            drawLineWithText(
                canvas, secondRect.left + secondRect.width() / 2, secondRect.top,
                secondRect.left + secondRect.width() / 2, firstRect.top
            )
            drawLineWithText(
                canvas, secondRect.left + secondRect.width() / 2, secondRect.bottom,
                secondRect.left + secondRect.width() / 2, firstRect.bottom
            )
        }
    }

    fun getTextHeight(paint: Paint, text: String): Float {
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        return rect.height().toFloat()
    }

    fun getTextWidth(paint: Paint, text: String?): Float {
        return paint.measureText(text)
    }
}