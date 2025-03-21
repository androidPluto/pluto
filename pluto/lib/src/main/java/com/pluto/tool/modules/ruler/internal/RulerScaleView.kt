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
import com.pluto.utilities.extensions.twoDecimal
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * A custom view that implements the ruler scale functionality.
 *
 * This view provides an interactive ruler that allows users to measure distances on the screen.
 * It handles touch events to track coordinates, displays measurement lines and values,
 * and supports both horizontal and vertical measurements. The ruler includes scale markers
 * at regular intervals and displays the measurement value in density-independent pixels (dp).
 *
 * @param context The context used to access resources and system services
 */
internal class RulerScaleView(context: Context) : View(context) {

    /**
     * The minimum distance that the user's finger must move to be considered a drag operation.
     * This helps distinguish between taps and drags.
     */
    private val touchSlop: Int

    /**
     * Coordinates where the ACTION_DOWN event occurred.
     */
    private var downCoordinate = CoordinatePair()

    /**
     * Current touch coordinates during a touch event.
     */
    private var lastTouchCoordinate = CoordinatePair()

    /**
     * Coordinates where the user clicked to place the ruler.
     */
    private var clickCoordinate = CoordinatePair()

    /**
     * Coordinates of the previous ruler position before the current movement.
     */
    private var prevCoordinate = CoordinatePair()

    /**
     * Coordinates where a movement operation started.
     */
    private var moveStartCoordinate = CoordinatePair()

    /**
     * Dimensions of the screen in dp.
     */
    private var screen = ScreenMeasurement()

    /**
     * Collection of Paint objects used for drawing the ruler components.
     */
    private val paintType = PaintType(context)

    /**
     * Current direction of ruler movement (Idle, Horizontal, or Vertical).
     */
    private var direction: Direction = Direction.Idle

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val vc = ViewConfiguration.get(context)
        touchSlop = vc.scaledTouchSlop
    }

    /**
     * Called to determine the size requirements for this view and its children.
     * Updates the screen measurement values based on the measured dimensions.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        screen.height = measuredHeight.toFloat().px2dp.toInt()
        screen.width = measuredWidth.toFloat().px2dp.toInt()
    }

    /**
     * Handles touch events to implement the ruler's interactive behavior.
     * Processes ACTION_DOWN, ACTION_MOVE, and ACTION_UP events to track coordinates
     * and update the ruler position and measurements.
     *
     * @param event The motion event containing touch information
     * @return true if the event was handled, false otherwise
     */
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

    /**
     * Draws the ruler components on the canvas.
     * This includes the initial scale, scroll indicators, and previous scale position.
     *
     * @param canvas The canvas on which to draw the ruler
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawInitialScale(canvas, screen)
        drawScroll(canvas)
        drawPreviousScale(canvas)
    }

    /**
     * Draws the initial ruler scale with boundary and scale markers.
     *
     * @param canvas The canvas on which to draw
     * @param screen The screen measurement information
     */
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
            i += SCALE_GAP
        }
        var j = 0
        while (j < screen.width) {
            canvas.drawLine(j.toFloat().dp2px, clickCoordinate.y, j.toFloat().dp2px, clickCoordinate.y + getMarkerHeight(j), paintType.scaleMarker)
            j += SCALE_GAP
        }
    }

    /**
     * Draws the previous scale position as dashed lines.
     *
     * @param canvas The canvas on which to draw
     */
    private fun drawPreviousScale(canvas: Canvas) {
        if (prevCoordinate.x > 0) {
            canvas.drawLine(prevCoordinate.x, 0f, prevCoordinate.x, measuredHeight.toFloat(), paintType.prevScale)
        }
        if (prevCoordinate.y > 0) {
            canvas.drawLine(0f, prevCoordinate.y, measuredWidth.toFloat(), prevCoordinate.y, paintType.prevScale)
        }
    }

    /**
     * Draws the measurement lines and text during a scroll/drag operation.
     * Shows different UI based on whether the movement is horizontal or vertical.
     *
     * @param canvas The canvas on which to draw
     */
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
            canvas.drawText("${dis.px2dp.twoDecimal} dp", clickCoordinate.x + dis / 2, clickCoordinate.y - 12f.dp, paintType.measurement)
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
            canvas.drawText("${dis.px2dp.twoDecimal} dp", clickCoordinate.x + 12f.dp, clickCoordinate.y + dis / 2, paintType.measurement)
        }
    }

    /**
     * Handles the ACTION_UP touch event.
     * Updates the ruler position based on the touch event and current direction.
     *
     * @param event The motion event containing touch information
     */
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

    /**
     * Handles the ACTION_MOVE touch event.
     * Determines the direction of movement and updates coordinates accordingly.
     *
     * @param event The motion event containing touch information
     */
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

    /**
     * Handles the ACTION_DOWN touch event.
     * Records the initial touch coordinates.
     *
     * @param event The motion event containing touch information
     */
    private fun handleActionDown(event: MotionEvent) {
        lastTouchCoordinate.x = event.x
        downCoordinate.x = lastTouchCoordinate.x
        lastTouchCoordinate.y = event.y
        downCoordinate.y = lastTouchCoordinate.y
    }

    /**
     * Determines the height of a scale marker based on its position.
     * Creates a pattern of different sized markers to improve readability.
     *
     * @param position The position along the scale
     * @return The height of the marker in pixels
     */
    private fun getMarkerHeight(position: Int): Int {
        return when {
            position / SCALE_GAP % (MARKER_SPIKE_INDICATOR_INDEX * 2) == 0 -> MID_MARKER_HEIGHT.roundToInt()
            position / SCALE_GAP % MARKER_SPIKE_INDICATOR_INDEX == 0 -> LARGE_MARKER_HEIGHT.roundToInt()
            else -> MARKER_HEIGHT.roundToInt()
        }
    }

    /**
     * Sealed class representing the possible directions of ruler movement.
     */
    private sealed class Direction {
        /** No movement is occurring */
        object Idle : Direction()

        /** Horizontal movement is occurring */
        object Horizontal : Direction()

        /** Vertical movement is occurring */
        object Vertical : Direction()
    }

    companion object {
        /** Index used to determine which markers should be larger */
        private const val MARKER_SPIKE_INDICATOR_INDEX = 5

        /** Gap between scale markers in dp */
        const val SCALE_GAP = 5

        /** Height of standard scale markers */
        private val MARKER_HEIGHT = 4f.dp2px

        /** Height of medium scale markers */
        private val MID_MARKER_HEIGHT = MARKER_HEIGHT * 1.6

        /** Height of large scale markers */
        private val LARGE_MARKER_HEIGHT = MARKER_HEIGHT * 2.2
    }
}
