package com.mocklets.pluto.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.mocklets.pluto.R

/**
 * A not-so-efficient rounded-rectangle-clipped FrameLayout, until Android-L's clipping Outlines are
 * widely available.
 *
 * NOTE: This doesn't support anti-aliasing, if you just need a rounded ImageView use the more
 * efficient BitmapDrawable method which does: http://evel.io/2013/07/21/rounded-avatars-in-android/
 *
 * In my case the rounded corner was to mask an image as you scrolled them in a ViewPager, so by
 * combining this with a RoundedImageView it appears anti-aliased unless actively scrolling.
 *
 * Created by richardleggett on 04/09/2014.
 */
internal class RoundedFrameLayout : FrameLayout {

    private var path: Path? = null
    private var rect: RectF? = null
    private var radiiArray = floatArrayOf(DEF_RADIUS, DEF_RADIUS, DEF_RADIUS, DEF_RADIUS, DEF_RADIUS, DEF_RADIUS, DEF_RADIUS, DEF_RADIUS)

    constructor(context: Context) : super(context, null, 0) {
        initView(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initView(attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun initView(attributes: AttributeSet?) {
        if (attributes != null) {
            val array = context.obtainStyledAttributes(
                attributes,
                R.styleable.pluto___RoundedFrameLayout
            )
            val mCornerRadius =
                array.getDimension(R.styleable.pluto___RoundedFrameLayout_corner_radius, -1f)
            if (mCornerRadius != -1f) {
                for (i in 0 until NO_OF_CORNERS) {
                    radiiArray[i] = mCornerRadius
                }
            } else {
                val topLeft =
                    array.getDimension(R.styleable.pluto___RoundedFrameLayout_corner_radius_left_top, DEF_RADIUS)
                val topRight =
                    array.getDimension(R.styleable.pluto___RoundedFrameLayout_corner_radius_right_top, DEF_RADIUS)
                val bottomRight =
                    array.getDimension(
                        R.styleable.pluto___RoundedFrameLayout_corner_radius_right_bottom,
                        DEF_RADIUS
                    )
                val bottomLeft =
                    array.getDimension(R.styleable.pluto___RoundedFrameLayout_corner_radius_left_bottom, DEF_RADIUS)
                radiiArray = floatArrayOf(
                    topLeft,
                    topLeft,
                    topRight,
                    topRight,
                    bottomRight,
                    bottomRight,
                    bottomLeft,
                    bottomLeft
                )
            }
            array.recycle()
        }

        path = Path()
        rect = RectF()

        setWillNotDraw(false)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        path?.let { path ->
            path.reset()
            rect?.let { rect ->
                rect.set(0f, 0f, w.toFloat(), h.toFloat())
                path.addRoundRect(rect, radiiArray, Path.Direction.CCW)
            }
            path.close()
        }
    }

    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        val count = canvas.save()
        canvas.clipPath(path!!)
        val result = super.drawChild(canvas, child, drawingTime)
        canvas.restoreToCount(count)
        return result
    }

    private companion object {
        const val DEF_RADIUS = 0f
        const val NO_OF_CORNERS = 8
    }
}
