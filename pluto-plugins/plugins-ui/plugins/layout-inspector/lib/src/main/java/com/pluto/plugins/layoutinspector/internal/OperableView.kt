package com.pluto.plugins.layoutinspector.internal

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewConfiguration
import com.pluto.plugins.layoutinspector.internal.canvas.ClickInfoCanvas
import com.pluto.plugins.layoutinspector.internal.canvas.GridCanvas
import com.pluto.plugins.layoutinspector.internal.canvas.RelativeCanvas
import com.pluto.plugins.layoutinspector.internal.canvas.SelectCanvas
import com.pluto.utilities.extensions.dp2px

internal class OperableView : View {

    private var touchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
    private var gridCanvas: GridCanvas = GridCanvas(this)
    private var clickInfoCanvas: ClickInfoCanvas = ClickInfoCanvas(this)
    private var relativeCanvas: RelativeCanvas = RelativeCanvas(this)
    private var selectCanvas: SelectCanvas = SelectCanvas(this)
    private var tapTimeout: Int = ViewConfiguration.getTapTimeout()
    private var longPressTimeout: Int = ViewConfiguration.getLongPressTimeout()

    @State
    private val state = 0

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

    @Retention(AnnotationRetention.SOURCE)
    annotation class State {
        companion object {
            var NONE = 0x00
            var PRESSING = 0x01 // after tapTimeout and before longPressTimeout
            var TOUCHING = 0x02 // trigger move before dragging
            var DRAGGING = 0x03 // since long press
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), defPaint)
        if (state === State.DRAGGING) {
            gridCanvas.draw(canvas, 1f)
        } else if (state === State.PRESSING) {
            gridCanvas.draw(canvas, alpha)
        }
//        selectCanvas.draw(canvas, relativeElements)
//        relativeCanvas.draw(
//            canvas, relativeElements.get(searchCount % elementsNum),
//            relativeElements.get(Math.abs(searchCount - 1) % elementsNum)
//        )
        clickInfoCanvas.draw(canvas)
    }

}
