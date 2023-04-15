package com.pluto.tool.modules.ruler.internal

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.extensions.dp2px
import com.pluto.utilities.extensions.px2dp
import com.pluto.utilities.settings.SettingsPreferences
import kotlin.math.abs
import kotlin.math.roundToInt

internal class RulerScaleView(context: Context) : View(context) {

    private val touchSlop: Int
    private var downCoordinate = CoordinatePair() // action down coordinate
    private var lastTouchCoordinate = CoordinatePair() // touch coordinate
    private var clickCoordinate = CoordinatePair() // click coordinate
    private var prevCoordinate = CoordinatePair() // before event coordinate
    private var moveStartCoordinate = CoordinatePair() // move start coordinate
    private var screen = ScreenMeasurement()
    private val paintType = PaintType(context)

    private var direction: Direction = Direction.Idle

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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                handleActionDown(event)
                super.onTouchEvent(event)
                return true
            }
            MotionEvent.ACTION_MOVE -> handleActionMove(event)
            MotionEvent.ACTION_UP -> handleActionUp(event)
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawInitialScale(canvas, screen)
        drawScroll(canvas)
        drawPreviousScale(canvas)
    }

    private fun drawInitialScale(canvas: Canvas, screen: ScreenMeasurement) {
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paintType.boundary)

        // init
        if (clickCoordinate.y > 0) {
            canvas.drawLine(0f, clickCoordinate.y, measuredWidth.toFloat(), clickCoordinate.y, paintType.scale)
        }
        if (clickCoordinate.x > 0) {
            canvas.drawLine(clickCoordinate.x, 0f, clickCoordinate.x, measuredHeight.toFloat(), paintType.scale)
        }

        // scale
        var i = 0
        while (i < screen.height) {
            canvas.drawLine(clickCoordinate.x, i.toFloat().dp2px, clickCoordinate.x + getMarkerHeight(i), i.toFloat().dp2px, paintType.scaleMarker)
            i += SettingsPreferences.gridSize
        }
        var j = 0
        while (j < screen.width) {
            canvas.drawLine(j.toFloat().dp2px, clickCoordinate.y, j.toFloat().dp2px, clickCoordinate.y + getMarkerHeight(j), paintType.scaleMarker)
            j += SettingsPreferences.gridSize
        }
    }

    private fun drawPreviousScale(canvas: Canvas) {
        if (prevCoordinate.x > 0) {
            canvas.drawLine(prevCoordinate.x, 0f, prevCoordinate.x, measuredHeight.toFloat(), paintType.prevScale)
        }
        if (prevCoordinate.y > 0) {
            canvas.drawLine(0f, prevCoordinate.y, measuredWidth.toFloat(), prevCoordinate.y, paintType.prevScale)
        }
    }

    private fun drawScroll(canvas: Canvas) {
        if (direction == Direction.Horizontal) {
            canvas.drawLine(
                clickCoordinate.x + lastTouchCoordinate.x - moveStartCoordinate.x,
                0f,
                clickCoordinate.x + lastTouchCoordinate.x - moveStartCoordinate.x,
                measuredHeight.toFloat(),
                paintType.scale
            )
            val dis = lastTouchCoordinate.x - moveStartCoordinate.x
            canvas.drawLine(clickCoordinate.x, clickCoordinate.y, clickCoordinate.x + dis, clickCoordinate.y, paintType.measurement)
            paintType.measurement.textAlign = Paint.Align.CENTER
            canvas.drawText("${dis.px2dp} dp", clickCoordinate.x + dis / 2, clickCoordinate.y - 12f.dp, paintType.measurement)
        } else if (direction == Direction.Vertical) {
            canvas.drawLine(
                0f,
                clickCoordinate.y + lastTouchCoordinate.y - moveStartCoordinate.y,
                measuredWidth.toFloat(),
                clickCoordinate.y + lastTouchCoordinate.y - moveStartCoordinate.y,
                paintType.scale
            )
            val dis = lastTouchCoordinate.y - moveStartCoordinate.y
            canvas.drawLine(clickCoordinate.x, clickCoordinate.y, clickCoordinate.x, clickCoordinate.y + dis, paintType.measurement)
            paintType.measurement.textAlign = Paint.Align.LEFT
            canvas.drawText("${dis.px2dp} dp", clickCoordinate.x + 12f.dp, clickCoordinate.y + dis / 2, paintType.measurement)
        }
    }

    private fun handleActionUp(event: MotionEvent) {
        if (direction == Direction.Idle) {
            prevCoordinate.y = 0f
            prevCoordinate.x = prevCoordinate.y
            clickCoordinate.x = event.x
            clickCoordinate.y = event.y
        } else {
            if (direction == Direction.Horizontal) {
                prevCoordinate.x = clickCoordinate.x
                clickCoordinate.x += event.x - moveStartCoordinate.x
            } else if (direction == Direction.Vertical) {
                prevCoordinate.y = clickCoordinate.y
                clickCoordinate.y += event.y - moveStartCoordinate.y
            }
            direction = Direction.Idle
        }
        invalidate()
    }

    private fun handleActionMove(event: MotionEvent) {
        lastTouchCoordinate.x = event.x
        lastTouchCoordinate.y = event.y
        val dx = lastTouchCoordinate.x - downCoordinate.x
        val dy = lastTouchCoordinate.y - downCoordinate.y
        if (direction == Direction.Idle) {
            if (abs(dx) > touchSlop) {
                direction = Direction.Horizontal
                moveStartCoordinate.x = lastTouchCoordinate.x
                prevCoordinate.x = clickCoordinate.x
                if (clickCoordinate.y <= 0) {
                    clickCoordinate.y = lastTouchCoordinate.y
                }
            } else if (abs(dy) > touchSlop) {
                direction = Direction.Vertical
                moveStartCoordinate.y = lastTouchCoordinate.y
                prevCoordinate.y = clickCoordinate.y
                if (clickCoordinate.x <= 0) {
                    clickCoordinate.x = lastTouchCoordinate.x
                }
            }
        }
        if (direction != Direction.Idle) {
            invalidate()
        }
    }

    private fun handleActionDown(event: MotionEvent) {
        lastTouchCoordinate.x = event.x
        downCoordinate.x = lastTouchCoordinate.x
        lastTouchCoordinate.y = event.y
        downCoordinate.y = lastTouchCoordinate.y
    }

    private fun getMarkerHeight(position: Int): Int {
        return when {
            position / SettingsPreferences.gridSize % (MARKER_SPIKE_INDICATOR_INDEX * 2) == 0 -> MID_MARKER_HEIGHT.roundToInt()
            position / SettingsPreferences.gridSize % MARKER_SPIKE_INDICATOR_INDEX == 0 -> LARGE_MARKER_HEIGHT.roundToInt()
            else -> MARKER_HEIGHT.roundToInt()
        }
    }

    private sealed class Direction {
        object Idle : Direction()
        object Horizontal : Direction()
        object Vertical : Direction()
    }

    private companion object {
        const val MARKER_SPIKE_INDICATOR_INDEX = 5
        val MARKER_HEIGHT = 4f.dp2px
        val MID_MARKER_HEIGHT = MARKER_HEIGHT * 1.6
        val LARGE_MARKER_HEIGHT = MARKER_HEIGHT * 2.2
    }
}
