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

    private var layerCount = 0

    fun setLayerCount(layerCount: Int) {
        this.layerCount = layerCount
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
    private val colorList: List<Int> = arrayListOf(
        Color.BLACK,
        Color.parseColor("#546E7A"),
        Color.parseColor("#FF1744"),
        Color.parseColor("#FFA000"),
        Color.parseColor("#1DE9B6"),
        Color.parseColor("#03A9F4"),
        Color.parseColor("#00E5FF"),
        Color.parseColor("#388E3C"),
        Color.parseColor("#76FF03"),
        Color.parseColor("#EEFF41")
    )

    init {
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 1..layerCount) {
            paint.color = colorList[i % colorList.size]
            paint.strokeWidth = 1f.dp2px
            canvas.drawLine((i * interval).toFloat(), 0f, (i * interval).toFloat(), measuredHeight.toFloat(), paint)
        }
    }
}
