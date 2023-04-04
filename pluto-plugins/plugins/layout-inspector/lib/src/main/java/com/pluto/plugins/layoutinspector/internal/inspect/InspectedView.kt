package com.pluto.plugins.layoutinspector.internal.inspect

import android.graphics.Rect
import android.view.View

internal class InspectedView(val view: View) {

    private val originRect: Rect = Rect()
    val rect: Rect = Rect()
    private val location = IntArray(2)
    val parentElement: InspectedView?
        get() {
            val parentView: Any = view.parent
            return if (parentView is View) {
                InspectedView(parentView)
            } else {
                null
            }
        }

    init {
        reset()
        originRect.set(rect.left, rect.top, rect.right, rect.bottom)
    }

    fun reset() {
        view.getLocationOnScreen(location)
        val width = view.width
        val height = view.height

        val left = location[0]
        val right = left + width
        val top = location[1]
        val bottom = top + height

        rect.set(left, top, right, bottom)
    }

    fun offset(dx: Float, dy: Float) {
        view.translationX = view.translationX + dx
        view.translationY = view.translationY + dy
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val element = o as InspectedView
        return view == element.view
    }

    override fun hashCode(): Int {
        return view.hashCode()
    }
}
