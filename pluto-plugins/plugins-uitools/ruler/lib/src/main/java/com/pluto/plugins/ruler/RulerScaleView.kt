package com.pluto.plugins.ruler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import com.pluto.plugin.utilities.extensions.dp
import com.pluto.plugin.utilities.extensions.px
import kotlin.math.abs

class RulerScaleView(context: Context) : View(context) {

    private val touchSlop: Int

    // downEvent
    private var downX = 0f
    private var downY = 0f

    // touchEvent
    private var lastX = 0f
    private var lastY = 0f

    // clickEvent
    private var initX = 0f
    private var initY = 0f

    // before changed
    private var oldX = 0f
    private var oldY = 0f

    // moveStartEvent
    private var moveStartX = 0f
    private var moveStartY = 0f
    private var heightDP = 0
    private var widthDP = 0
    private val scaleLength: Int = 4f.px.toInt() // ViewKnife.dip2px(4f).toInt()
    private val scaleGap = 5 // (dp)

    // scroll direction
    @Direction
    private var direction = 0

    //    @IntDef(
//        Direction.NONE,
//        Direction.HORIZONTAL,
//        Direction.VERTICAL
//    )
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
            strokeWidth = 1f.px
        }
    }
    private val oldPaint: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.getColor(R.color.pluto___red_80)
            style = Style.STROKE
            strokeWidth = 1f.px
            pathEffect = DashPathEffect(floatArrayOf(3f.dp, 2f.dp), 0f)
        }
    }
    private val mutablePaint: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = context.getColor(R.color.pluto___blue)
            style = Style.FILL
            strokeWidth = 3f.px // ViewKnife.dip2px(2f)
            textSize = 14f.px // ViewKnife.dip2px(12f)
            typeface = ResourcesCompat.getFont(context, R.font.muli_semibold)
            flags = FAKE_BOLD_TEXT_FLAG
        }
    }
    private val defPaint: Paint = object : Paint(ANTI_ALIAS_FLAG) {
        init {
            color = Color.YELLOW
            strokeWidth = 2f.px // ViewKnife.dip2px(2f)
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
        heightDP = measuredHeight.toFloat().dp.toInt() // ViewKnife.px2dip(measuredHeight.toFloat())
        widthDP = measuredWidth.toFloat().dp.toInt() // ViewKnife.px2dip(measuredWidth.toFloat())
    }

    @SuppressWarnings("LongMethod", "ComplexMethod", "NestedBlockDepth")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                run {
                    lastX = event.x
                    downX = lastX
                }
                run {
                    lastY = event.y
                    downY = lastY
                }
                super.onTouchEvent(event)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                lastX = event.x
                lastY = event.y
                val dx = lastX - downX
                val dy = lastY - downY
                if (direction == Direction.NONE) {
                    if (abs(dx) > touchSlop) {
                        direction = Direction.HORIZONTAL
                        moveStartX = lastX
                        oldX = initX
                        if (initY <= 0) {
                            initY = lastY
                        }
                    } else if (abs(dy) > touchSlop) {
                        direction = Direction.VERTICAL
                        moveStartY = lastY
                        oldY = initY
                        if (initX <= 0) {
                            initX = lastX
                        }
                    }
                }
                if (direction != Direction.NONE) {
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                if (direction == Direction.NONE) {
                    oldY = 0f
                    oldX = oldY
                    initX = event.x
                    initY = event.y
                } else {
                    if (direction == Direction.HORIZONTAL) {
                        oldX = initX
                        initX += event.x - moveStartX
                    } else if (direction == Direction.VERTICAL) {
                        oldY = initY
                        initY += event.y - moveStartY
                    }
                    direction = Direction.NONE
                }
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), defPaint)

        // init
        if (initY > 0) {
            canvas.drawLine(0f, initY, measuredWidth.toFloat(), initY, paint)
        }
        if (initX > 0) {
            canvas.drawLine(initX, 0f, initX, measuredHeight.toFloat(), paint)
        }
        // scale
        run {
            var i = 0
            while (i < heightDP) {
                canvas.drawLine(initX, i.toFloat().dp, initX + scaleLength, i.toFloat().dp, paint)
                i += scaleGap
            }
        }
        var i = 0
        while (i < widthDP) {
            canvas.drawLine(i.toFloat().dp, initY, i.toFloat().dp, initY + scaleLength, paint)
            i += scaleGap
        }
        // scroll
        if (direction == Direction.HORIZONTAL) {
            canvas.drawLine(initX + lastX - moveStartX, 0f, initX + lastX - moveStartX, measuredHeight.toFloat(), paint)
            val dis = lastX - moveStartX
            canvas.drawLine(initX, initY, initX + dis, initY, mutablePaint)
            mutablePaint.textAlign = Paint.Align.CENTER
            canvas.drawText("${dis.dp} dp", initX + dis / 2, initY - 12f.dp, mutablePaint)
        } else if (direction == Direction.VERTICAL) {
            canvas.drawLine(0f, initY + lastY - moveStartY, measuredWidth.toFloat(), initY + lastY - moveStartY, paint)
            val dis = lastY - moveStartY
            canvas.drawLine(initX, initY, initX, initY + dis, mutablePaint)
            mutablePaint.textAlign = Paint.Align.LEFT
            canvas.drawText("${dis.dp} dp", initX + 12f.dp, initY + dis / 2, mutablePaint)
        }
        // old
        if (oldX > 0) {
            canvas.drawLine(oldX, 0f, oldX, measuredHeight.toFloat(), oldPaint)
        }
        if (oldY > 0) {
            canvas.drawLine(0f, oldY, measuredWidth.toFloat(), oldY, oldPaint)
        }
    }
}
