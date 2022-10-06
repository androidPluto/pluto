package com.pluto.plugins.uitools.ruler.internal

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import com.pluto.plugin.utilities.extensions.dp
import com.pluto.plugin.utilities.extensions.dp2px
import com.pluto.plugin.utilities.extensions.px2dp
import com.pluto.plugins.uitools.R
import kotlin.math.abs

internal class RulerScaleView(context: Context) : View(context) {

    private val touchSlop: Int

    private var downCoordinate = CoordinatePair() // action down coordinate
    private var lastTouchCoordinate = CoordinatePair() // touch coordinate
    private var clickCoordinate = CoordinatePair() // click coordinate
    private var prevCoordinate = CoordinatePair() // before event coordinate
    private var moveStartCoordinate = CoordinatePair() // move start coordinate

    private var heightDP = 0
    private var widthDP = 0
    private val scaleLength: Int = 4f.dp2px.toInt() // ViewKnife.dip2px(4f).toInt()
    private val scaleGap = 5 // (dp)

    // scroll direction
    @Direction
    private var direction = 0

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Direction {
        companion object {
            var NONE = 0x00
            var HORIZONTAL = 0x01
            var VERTICAL = 0x02
        }
    }

    private val paint: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.getColor(R.color.pluto___red_dark)
            style = Style.FILL
            strokeWidth = 1f.dp2px
        }
    }

    private val spikePain: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.getColor(R.color.pluto___red_80)
            style = Style.FILL
            strokeWidth = 1f.dp2px
        }
    }

    private val oldPaint: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.getColor(R.color.pluto___red_80)
            style = Style.STROKE
            strokeWidth = 1f.dp2px
            pathEffect = DashPathEffect(floatArrayOf(3f.dp, 2f.dp), 0f)
        }
    }
    private val mutablePaint: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.getColor(R.color.pluto___colorPrimaryDark)
            style = Style.FILL
            strokeWidth = 4f.dp2px // ViewKnife.dip2px(2f)
            textSize = 14f.dp2px // ViewKnife.dip2px(12f)
            typeface = ResourcesCompat.getFont(context, R.font.muli_semibold)
            flags = FAKE_BOLD_TEXT_FLAG
        }
    }
    private val defPaint: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.getColor(R.color.pluto___emerald)
            strokeWidth = 2f.dp2px // ViewKnife.dip2px(2f)
            style = Style.STROKE
        }
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val vc = ViewConfiguration.get(context)
        touchSlop = vc.scaledTouchSlop
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        heightDP = measuredHeight.toFloat().px2dp.toInt() // ViewKnife.px2dip(measuredHeight.toFloat())
        widthDP = measuredWidth.toFloat().px2dp.toInt() // ViewKnife.px2dip(measuredWidth.toFloat())
    }

    @SuppressWarnings("LongMethod", "ComplexMethod", "NestedBlockDepth")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                run {
                    lastTouchCoordinate.x = event.x
                    downCoordinate.x = lastTouchCoordinate.x
                }
                run {
                    lastTouchCoordinate.y = event.y
                    downCoordinate.y = lastTouchCoordinate.y
                }
                super.onTouchEvent(event)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                lastTouchCoordinate.x = event.x
                lastTouchCoordinate.y = event.y
                val dx = lastTouchCoordinate.x - downCoordinate.x
                val dy = lastTouchCoordinate.y - downCoordinate.y
                if (direction == Direction.NONE) {
                    if (abs(dx) > touchSlop) {
                        direction = Direction.HORIZONTAL
                        moveStartCoordinate.x = lastTouchCoordinate.x
                        prevCoordinate.x = clickCoordinate.x
                        if (clickCoordinate.y <= 0) {
                            clickCoordinate.y = lastTouchCoordinate.y
                        }
                    } else if (abs(dy) > touchSlop) {
                        direction = Direction.VERTICAL
                        moveStartCoordinate.y = lastTouchCoordinate.y
                        prevCoordinate.y = clickCoordinate.y
                        if (clickCoordinate.x <= 0) {
                            clickCoordinate.x = lastTouchCoordinate.x
                        }
                    }
                }
                if (direction != Direction.NONE) {
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                if (direction == Direction.NONE) {
                    prevCoordinate.y = 0f
                    prevCoordinate.x = prevCoordinate.y
                    clickCoordinate.x = event.x
                    clickCoordinate.y = event.y
                } else {
                    if (direction == Direction.HORIZONTAL) {
                        prevCoordinate.x = clickCoordinate.x
                        clickCoordinate.x += event.x - moveStartCoordinate.x
                    } else if (direction == Direction.VERTICAL) {
                        prevCoordinate.y = clickCoordinate.y
                        clickCoordinate.y += event.y - moveStartCoordinate.y
                    }
                    direction = Direction.NONE
                }
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    @SuppressWarnings("LongMethod")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), defPaint)

        // init
        if (clickCoordinate.y > 0) {
            canvas.drawLine(0f, clickCoordinate.y, measuredWidth.toFloat(), clickCoordinate.y, paint)
        }
        if (clickCoordinate.x > 0) {
            canvas.drawLine(clickCoordinate.x, 0f, clickCoordinate.x, measuredHeight.toFloat(), paint)
        }
        // scale

        var i = 0f
        while (i < heightDP) {
            val scLength = if (i / scaleGap % SPIKE_INDICATOR_INDEX > 0) {
                scaleLength
            } else {
                scaleLength * 2
            }
            canvas.drawLine(clickCoordinate.x, i.dp2px, clickCoordinate.x + scLength, i.dp2px, spikePain)
            i += scaleGap
        }
        var j = 0f
        while (j < widthDP) {
            val scLength = if (j / scaleGap % SPIKE_INDICATOR_INDEX > 0) {
                scaleLength
            } else {
                scaleLength * 2
            }
            canvas.drawLine(j.dp2px, clickCoordinate.y, j.dp2px, clickCoordinate.y + scLength, spikePain)
            j += scaleGap
        }
        // scroll
        if (direction == Direction.HORIZONTAL) {
            canvas.drawLine(
                clickCoordinate.x + lastTouchCoordinate.x - moveStartCoordinate.x,
                0f,
                clickCoordinate.x + lastTouchCoordinate.x - moveStartCoordinate.x,
                measuredHeight.toFloat(),
                paint
            )
            val dis = lastTouchCoordinate.x - moveStartCoordinate.x
            canvas.drawLine(clickCoordinate.x, clickCoordinate.y, clickCoordinate.x + dis, clickCoordinate.y, mutablePaint)
            mutablePaint.textAlign = Paint.Align.CENTER
            canvas.drawText("${dis.px2dp} dp", clickCoordinate.x + dis / 2, clickCoordinate.y - 12f.dp, mutablePaint)
        } else if (direction == Direction.VERTICAL) {
            canvas.drawLine(
                0f,
                clickCoordinate.y + lastTouchCoordinate.y - moveStartCoordinate.y,
                measuredWidth.toFloat(),
                clickCoordinate.y + clickCoordinate.y - moveStartCoordinate.y,
                paint
            )
            val dis = lastTouchCoordinate.y - moveStartCoordinate.y
            canvas.drawLine(clickCoordinate.x, clickCoordinate.y, clickCoordinate.x, clickCoordinate.y + dis, mutablePaint)
            mutablePaint.textAlign = Paint.Align.LEFT
            canvas.drawText("${dis.px2dp} dp", clickCoordinate.x + 12f.dp, clickCoordinate.y + dis / 2, mutablePaint)
        }
        // old
        if (prevCoordinate.x > 0) {
            canvas.drawLine(prevCoordinate.x, 0f, prevCoordinate.x, measuredHeight.toFloat(), oldPaint)
        }
        if (prevCoordinate.y > 0) {
            canvas.drawLine(0f, prevCoordinate.y, measuredWidth.toFloat(), prevCoordinate.y, oldPaint)
        }
    }

    private companion object {
        const val SPIKE_INDICATOR_INDEX = 5
    }
}
