package com.pluto.utilities.list

import android.content.Context
import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.pluto.plugin.R
import com.pluto.utilities.extensions.drawable

class CustomItemDecorator(context: Context, private val edge: Int = 0) : ItemDecoration() {

    private val divider = context.drawable(R.drawable.pluto___line_divider)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + edge
        val right = parent.width - parent.paddingRight - edge
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top: Int = child.bottom + params.bottomMargin
            val bottom = top + (divider?.intrinsicHeight ?: 0)
            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }
}
