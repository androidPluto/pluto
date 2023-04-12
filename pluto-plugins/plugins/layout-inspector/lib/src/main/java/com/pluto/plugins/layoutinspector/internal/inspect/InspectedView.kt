package com.pluto.plugins.layoutinspector.internal.inspect

import android.graphics.Rect
import android.view.View

internal class InspectedView(val view: View) {

    private val originRect: Rect = Rect()
    val rect: Rect = Rect()
    private val location = IntArray(2)
    val parent: InspectedView?
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
        val left = location[0]
        val right = left + view.width
        val top = location[1]
        val bottom = top + view.height
        rect.set(left, top, right, bottom)
    }

    fun offset(dx: Float, dy: Float) {
        view.translationX = view.translationX + dx
        view.translationY = view.translationY + dy
    }

    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other == null || javaClass != other.javaClass) return false
        return other is InspectedView && view == other.view
    }

    override fun hashCode(): Int = view.hashCode()
}
