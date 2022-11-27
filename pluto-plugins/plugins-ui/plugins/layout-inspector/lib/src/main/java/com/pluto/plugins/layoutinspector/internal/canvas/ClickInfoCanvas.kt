package com.pluto.plugins.layoutinspector.internal.canvas

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import com.pluto.plugins.layoutinspector.internal.Element
import com.pluto.utilities.extensions.dp2px
import com.pluto.utilities.extensions.px2dp

internal class ClickInfoCanvas(private val container: View, private val showInfoAlways: Boolean = false) {

    private val cornerRadius = 1.5f.dp2px
    private val textBgFillingSpace = 3f.dp2px
    private val textLineDistance = 6f.dp2px
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
            color = Color.WHITE
            style = Style.FILL
        }
    }
    private val tmpRectF: RectF = RectF()
    private var infoElement: Element? = null
    private var infoAnimator: ValueAnimator? = null
    fun setInfoElement(infoElement: Element?) {
        this.infoElement = infoElement
        if (!showInfoAlways) {
            animInfo()
        }
    }

    private fun animInfo() {
        if (infoAnimator != null) {
            infoAnimator?.removeAllUpdateListeners()
            infoAnimator?.cancel()
        }
        infoAnimator = ObjectAnimator.ofInt(255, 0).setDuration(1400)
        infoAnimator?.addUpdateListener { container.invalidate() }
        infoAnimator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                infoElement = null
                container.invalidate()
            }
        })
        infoAnimator?.start()
    }

    fun draw(canvas: Canvas) {
        if (infoElement == null) {
            return
        }
        var show = showInfoAlways
        if (!show) {
            show = infoAnimator?.isRunning ?: false
        }
        if (show) {
            val alpha: Int = if (showInfoAlways) 255 else ((infoAnimator?.animatedValue ?: 0) as Int)
            cornerPaint.alpha = alpha
            textPaint.alpha = alpha
            val rect: Rect = infoElement!!.rect
            val widthText = "${rect.width().toFloat().px2dp} dp"
            drawText(canvas, widthText, rect.centerX() - getTextWidth(textPaint, widthText) / 2, (rect.top - textLineDistance))
            val heightText = "${rect.height().toFloat().px2dp} dp"
            drawText(canvas, heightText, (rect.right + textLineDistance), rect.centerY().toFloat())
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
        tmpRectF.set(left, top, right, bottom)
        canvas.drawRoundRect(tmpRectF, cornerRadius, cornerRadius, cornerPaint)
        canvas.drawText(text, left + textBgFillingSpace, bottom - textBgFillingSpace, textPaint)
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
