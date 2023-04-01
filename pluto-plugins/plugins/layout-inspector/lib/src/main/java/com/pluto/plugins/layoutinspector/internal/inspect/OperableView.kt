package com.pluto.plugins.layoutinspector.internal.inspect

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.Toast
import com.pluto.plugins.layoutinspector.internal.CoordinatePair
import com.pluto.plugins.layoutinspector.internal.OperableViewState
import com.pluto.plugins.layoutinspector.internal.canvas.ClickInfoCanvas
import com.pluto.plugins.layoutinspector.internal.canvas.GridCanvas
import com.pluto.plugins.layoutinspector.internal.canvas.RelativeCanvas
import com.pluto.plugins.layoutinspector.internal.canvas.SelectCanvas
import com.pluto.utilities.extensions.dp2px
import kotlin.math.abs

internal class OperableView : ElementHoldView {

    private var gridAnimator: ValueAnimator? = null
    private var targetElement: Element? = null
    private var touchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
    private var gridCanvas: GridCanvas = GridCanvas(this)
    private var clickInfoCanvas: ClickInfoCanvas = ClickInfoCanvas(this)
    private var relativeCanvas: RelativeCanvas = RelativeCanvas(this)
    private var selectCanvas: SelectCanvas = SelectCanvas(this)
    private var tapTimeout: Int = ViewConfiguration.getTapTimeout()
    private var longPressTimeout: Int = ViewConfiguration.getLongPressTimeout()

    private var prevCoordinate = CoordinatePair()
    private var downCoordinate = CoordinatePair()

    private var state: OperableViewState = OperableViewState.Idle
    private var searchCount = 0

    // max selectable count
    private val elementsNum = 2
    private var relativeElements = arrayOfNulls<Element>(elementsNum)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    private val defPaint: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = Color.YELLOW
            strokeWidth = 2f.dp2px
            style = Style.STROKE
        }
    }
    private val longPressCheck = Runnable {
        state = OperableViewState.Dragging // State.DRAGGING
        alpha = 1f
    }
    private val tapTimeoutCheck = Runnable {
        state = OperableViewState.Pressing // State.PRESSING
        gridAnimator = ObjectAnimator.ofFloat(0f, 1f)
            .setDuration((longPressTimeout - tapTimeout).toLong())
        gridAnimator?.addUpdateListener { animation ->
            alpha = animation.animatedValue as Float
            invalidate()
        }
        gridAnimator?.start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                handleActionDown(event)
                return true
            }

            MotionEvent.ACTION_MOVE -> handleActionMove(event)
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> handleActionCancel(event)
        }
        return super.onTouchEvent(event)
    }

    private fun handleActionCancel(event: MotionEvent) {
        cancelCheckTask()
        when (state) {
            is OperableViewState.Idle -> handleClick(event.x, event.y)
            is OperableViewState.Dragging -> resetAll()
            else -> {}
        }
        state = OperableViewState.Idle
        invalidate()
    }

    private fun handleActionMove(event: MotionEvent) {
        when (state) {
            is OperableViewState.Dragging -> targetElement?.let {
                val dx: Float = event.x - prevCoordinate.x
                val dy: Float = event.y - prevCoordinate.y
                it.offset(dx, dy)
                for (e in relativeElements) {
                    e?.reset()
                }
                invalidate()
            }

            is OperableViewState.Touching -> {}
            else -> {
                val dx: Float = event.x - downCoordinate.x
                val dy: Float = event.y - downCoordinate.y
                if (dx * dx + dy * dy > touchSlop * touchSlop) {
                    if (state is OperableViewState.Pressing) {
                        Toast.makeText(context, "CANCEL", Toast.LENGTH_SHORT).show()
                    }
                    state = OperableViewState.Touching
                    cancelCheckTask()
                    invalidate()
                }
            }
        }
        prevCoordinate.x = event.x
        prevCoordinate.y = event.y
    }

    private fun handleActionDown(event: MotionEvent) {
        run {
            prevCoordinate.x = event.x
            downCoordinate.x = event.x
        }
        run {
            prevCoordinate.y = event.y
            downCoordinate.y = event.y
        }
        tryStartCheckTask()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), defPaint)
        when (state) {
            is OperableViewState.Dragging -> gridCanvas.draw(canvas, 1f)
            is OperableViewState.Pressing -> gridCanvas.draw(canvas, alpha)
            else -> {}
        }
        selectCanvas.draw(canvas, *relativeElements)
        relativeCanvas.draw(
            canvas, relativeElements[searchCount % elementsNum],
            relativeElements[abs(searchCount - 1) % elementsNum]
        )
        clickInfoCanvas.draw(canvas)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelCheckTask()
        relativeElements = emptyArray()
    }

    private fun cancelCheckTask() {
        removeCallbacks(longPressCheck)
        removeCallbacks(tapTimeoutCheck)
        gridAnimator?.cancel()
        gridAnimator = null
    }

    private fun tryStartCheckTask() {
        cancelCheckTask()
        targetElement?.let {
            postDelayed(longPressCheck, longPressTimeout.toLong())
            postDelayed(tapTimeoutCheck, tapTimeout.toLong())
        }
    }

    private fun handleClick(x: Float, y: Float) {
        getTargetElement(x, y)?.let {
            handleElementSelected(it, true)
        }
    }

    fun handleClick(v: View): Boolean {
        return getTargetElement(v)?.let {
            handleElementSelected(it, false)
            invalidate()
            true
        } ?: run {
            false
        }
    }

    @SuppressWarnings("NestedBlockDepth")
    private fun handleElementSelected(element: Element, cancelIfSelected: Boolean) {
        targetElement = element
        var bothNull = true
        for (i in relativeElements.indices) {
            relativeElements[i]?.let {
                if (relativeElements[i] === element) {
                    if (cancelIfSelected) {
                        // cancel selected
                        relativeElements[i] = null
                        searchCount = i
                    }
                    clickListener?.onClick(element.view)
                    return
                }
                bothNull = false
            }
        }
        if (bothNull) {
            // If only one is selected, show info
            clickInfoCanvas.setInfoElement(element)
        }
        relativeElements[searchCount % elementsNum] = element
        searchCount++
        clickListener?.onClick(element.view)
    }

    fun isSelectedEmpty(): Boolean {
        var empty = true
        for (i in 0 until elementsNum) {
            if (relativeElements[i] != null) {
                empty = false
                break
            }
        }
        return empty
    }

    private var clickListener: OnClickListener? = null
    override fun setOnClickListener(l: OnClickListener?) {
        clickListener = l
    }
}
