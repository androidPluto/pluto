package com.pluto.plugins.layoutinspector.internal.inspect

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.core.view.children
import com.pluto.plugins.layoutinspector.internal.canvas.CaptureCanvas
import com.pluto.plugins.layoutinspector.internal.canvas.DimensionCanvas
import com.pluto.plugins.layoutinspector.internal.canvas.GridCanvas
import com.pluto.utilities.extensions.dp2px

internal class InspectOverlay : View {

    private var gridAnimator: ValueAnimator? = null
    private var touchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
    private var longPressTimeout: Int = ViewConfiguration.getLongPressTimeout()

    private var gridCanvas: GridCanvas = GridCanvas(this)
    private var dimenCanvas: DimensionCanvas = DimensionCanvas(this)
    private var captureCanvas: CaptureCanvas = CaptureCanvas(this)

    private var prevCoordinate = CoordinatePair()
    private var downCoordinate = CoordinatePair()

    private var state: State = State.Idle

    private val inspectedViews = arrayListOf<InspectedView>()
    private var targetInspectedView: InspectedView? = null

    private var clickListener: OnClickListener? = null

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
        state = State.Dragging
        alpha = 1f
        gridAnimator = ObjectAnimator.ofFloat(0f, 1f).setDuration(longPressTimeout.toLong())
        gridAnimator?.addUpdateListener { animation ->
            alpha = animation.animatedValue as Float
            invalidate()
        }
        gridAnimator?.start()
    }

    fun tryGetFrontView(targetActivity: Activity) {
        traverse(targetActivity.getFrontView())
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), defPaint)
        when (state) {
            is State.Dragging -> gridCanvas.draw(canvas)
            else -> {}
        }
        captureCanvas.draw(canvas, targetInspectedView)
        dimenCanvas.draw(canvas, targetInspectedView)
    }

    private fun handleActionCancel(event: MotionEvent) {
        cancelCheckTask()
        when (state) {
            is State.Idle -> handleClick(event.x, event.y)
            is State.Dragging -> resetAll()
            else -> {}
        }
        state = State.Idle
        invalidate()
    }

    private fun handleActionMove(event: MotionEvent) {
        when (state) {
            is State.Dragging -> targetInspectedView?.let {
                val dx: Float = event.x - prevCoordinate.x
                val dy: Float = event.y - prevCoordinate.y
                it.offset(dx, dy)
                targetInspectedView?.reset()
                invalidate()
            }

            is State.Touching -> {
            }

            else -> {
                val dx: Float = event.x - downCoordinate.x
                val dy: Float = event.y - downCoordinate.y
                if (dx * dx + dy * dy > touchSlop * touchSlop) {
                    state = State.Touching
                    cancelCheckTask()
                    invalidate()
                }
            }
        }
        prevCoordinate = CoordinatePair(event.x, event.y)
    }

    private fun handleActionDown(event: MotionEvent) {
        prevCoordinate = CoordinatePair(event.x, event.y)
        downCoordinate = CoordinatePair(event.x, event.y)
        tryStartCheckTask()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        inspectedViews.clear()
        cancelCheckTask()
        targetInspectedView = null
    }

    private fun cancelCheckTask() {
        removeCallbacks(longPressCheck)
        gridAnimator?.cancel()
        gridAnimator = null
    }

    private fun tryStartCheckTask() {
        cancelCheckTask()
        targetInspectedView?.let {
            postDelayed(longPressCheck, longPressTimeout.toLong())
        }
    }

    private fun handleClick(x: Float, y: Float) {
        getInspectedView(x, y)?.let { processViewInspection(it, true) }
    }

    fun handleClick(v: View, cancelIfSelected: Boolean): Boolean {
        return getInspectedView(v)?.let {
            processViewInspection(it, cancelIfSelected)
            invalidate()
            true
        } ?: run {
            false
        }
    }

    @SuppressWarnings("LoopWithTooManyJumpStatements")
    fun getInspectedView(x: Float, y: Float): InspectedView? {
        var target: InspectedView? = null
        for (i in inspectedViews.indices.reversed()) {
            val inspectedView = inspectedViews[i]
            if (inspectedView.rect.contains(x.toInt(), y.toInt())) {
                if (isParentNotVisible(inspectedView.parent)) {
                    continue
                }
                target = inspectedView
                break
            }
        }
        if (target == null) {
            Log.w(TAG, "getInspectedView: not find")
        }
        return target
    }

    private fun getInspectedView(v: View): InspectedView? {
        var target: InspectedView? = null
        for (i in inspectedViews.indices.reversed()) {
            val inspectedView = inspectedViews[i]
            if (inspectedView.view === v) {
                target = inspectedView
                break
            }
        }
        if (target == null) {
            Log.w(TAG, "getInspectedView: not find")
        }
        return target
    }

    private fun isParentNotVisible(parent: InspectedView?): Boolean {
        if (parent == null) {
            return false
        }
        return if (parent.rect.left >= measuredWidth || parent.rect.top >= measuredHeight) {
            true
        } else {
            isParentNotVisible(parent.parent)
        }
    }

    private fun processViewInspection(inspectedView: InspectedView, cancelIfSelected: Boolean) {
        targetInspectedView = if (targetInspectedView == inspectedView && cancelIfSelected) {
            null
        } else {
            inspectedView
        }
        dimenCanvas.inspectedView = targetInspectedView
        clickListener?.onClick(inspectedView.view)
    }

    fun isSelectedEmpty(): Boolean = targetInspectedView == null

    override fun setOnClickListener(l: OnClickListener?) {
        clickListener = l
    }

    companion object {
        private const val TAG = "InspectOverlay"
    }

    private data class CoordinatePair(val x: Float = 0f, val y: Float = 0f)

    private sealed class State {
        object Idle : State()
        object Touching : State() // trigger move before dragging
        object Dragging : State() // since long press
    }

    private fun traverse(view: View) {
        if (view.alpha == 0f || view.visibility != VISIBLE) return
        inspectedViews.add(InspectedView(view))
        if (view is ViewGroup) {
            view.children.forEach {
                traverse(it)
            }
        }
    }

    private fun resetAll() {
        inspectedViews.forEach { it.reset() }
    }
}
