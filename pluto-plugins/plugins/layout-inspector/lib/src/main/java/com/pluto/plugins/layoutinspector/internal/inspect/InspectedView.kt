package com.pluto.plugins.layoutinspector.internal.inspect

import android.graphics.Rect
import android.view.View

/**
 * Wraps an inspected [view] and maintains a [rect] in **overlay-local coordinates**
 * (i.e. relative to the top-left of [InspectOverlay]) so that hit-testing and canvas
 * drawing are both correct regardless of whether the host (Pluto) activity is running
 * edge-to-edge or not.
 *
 * @param overlay The [InspectOverlay] view. Its current screen position is subtracted
 *   from [view]'s screen position inside every [reset] call, so the stored [rect] is
 *   always in overlay-local space even after window-inset changes.
 */
internal class InspectedView(val view: View, private val overlay: View? = null) {

    private val originRect: Rect = Rect()
    val rect: Rect = Rect()
    private val location = IntArray(2)
    private val overlayLoc = IntArray(2)

    val parent: InspectedView?
        get() {
            val parentView: Any = view.parent
            return if (parentView is View) {
                InspectedView(parentView, overlay)
            } else {
                null
            }
        }

    init {
        reset()
        originRect.set(rect.left, rect.top, rect.right, rect.bottom)
    }

    fun reset() {
        // Always recompute the overlay's position so this works correctly even if called
        // before the first layout pass (overlayLoc will just be [0,0] then, harmlessly).
        overlay?.getLocationOnScreen(overlayLoc)
        view.getLocationOnScreen(location)
        // Subtract the overlay's screen offset → rect is in overlay-local space.
        // This cancels out the status-bar (and/or action-bar) offset introduced by the
        // Pluto activity's paddingTop when edge-to-edge is enabled.
        val left = location[0] - overlayLoc[0]
        val right = left + view.width
        val top = location[1] - overlayLoc[1]
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
