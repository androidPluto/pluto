package com.pluto.plugins.layoutinspector.internal.hierarchy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.utilities.extensions.dp2px

class TreeNodeLayout : ConstraintLayout {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    private var sysLayoutCount = 0
    private var layerCount = 0

    fun setLayerCount(layerCount: Int, sysLayoutCount: Int) {
        this.layerCount = layerCount
        this.sysLayoutCount = sysLayoutCount
        setPadding(interval * layerCount + 2f.dp2px.toInt(), paddingTop, paddingRight, paddingBottom)
        invalidate()
    }

    private val interval: Int = 10f.dp2px.toInt()
    private val paint: Paint = object : Paint() {
        init {
            color = Color.GRAY
            style = Style.FILL
            strokeWidth = 0.5f.dp2px
        }
    }
    private val color0x = Color.GRAY
    private val color1x = -0x3d2728
    private val color2x = -0x843e3c
    private val color3x = -0x8f3531
    private val color4x = -0x6f2e3f
    private val color5x = -0x632daf
    private val color6x = -0x122d97
    private val color7x = -0x1e5e99
    private val color8x = -0x136e88
    private val color9x = -0x1587ab
    private val color10x = -0x85c6dd
    private val color11x = Color.BLACK

    init {
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 1..layerCount) {
            if (i > sysLayoutCount) {
                if (i >= 11 + sysLayoutCount) {
                    paint.color = color11x
                } else if (i >= 10 + sysLayoutCount) {
                    paint.color = color10x
                } else if (i >= 9 + sysLayoutCount) {
                    paint.color = color9x
                } else if (i >= 8 + sysLayoutCount) {
                    paint.color = color8x
                } else if (i == 7 + sysLayoutCount) {
                    paint.color = color7x
                } else if (i == 6 + sysLayoutCount) {
                    paint.color = color6x
                } else if (i == 5 + sysLayoutCount) {
                    paint.color = color5x
                } else if (i == 4 + sysLayoutCount) {
                    paint.color = color4x
                } else if (i == 3 + sysLayoutCount) {
                    paint.color = color3x
                } else if (i == 2 + sysLayoutCount) {
                    paint.color = color2x
                } else if (i == 1 + sysLayoutCount) {
                    paint.color = color1x
                }
                paint.strokeWidth = 1f.dp2px
            } else {
                paint.strokeWidth = 0.5f.dp2px
                paint.color = color0x
            }
            canvas.drawLine((i * interval).toFloat(), 0f, (i * interval).toFloat(), measuredHeight.toFloat(), paint)
        }
    }
}
