package com.pluto.plugins.layoutinspector.internal.inspect

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup

internal open class ElementHoldView : View {

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    private val elements: MutableList<Element> = ArrayList()
    private fun traverse(view: View) {
        if (view.alpha == 0f || view.visibility != VISIBLE) return
        elements.add(Element(view))
        if (view is ViewGroup) {
            val parent: ViewGroup = view
            for (i in 0 until parent.childCount) {
                traverse(parent.getChildAt(i))
            }
        }
    }

    @SuppressWarnings("LoopWithTooManyJumpStatements")
    fun getTargetElement(x: Float, y: Float): Element? {
        var target: Element? = null
        for (i in elements.indices.reversed()) {
            val element = elements[i]
            if (element.rect.contains(x.toInt(), y.toInt())) {
                if (isParentNotVisible(element.parentElement)) {
                    continue
                }
                target = element
                break
            }
        }
        if (target == null) {
            Log.w(TAG, "getTargetElement: not find")
        }
        return target
    }

    protected fun getTargetElement(v: View): Element? {
        var target: Element? = null
        for (i in elements.indices.reversed()) {
            val element = elements[i]
            if (element.view === v) {
                target = element
                break
            }
        }
        if (target == null) {
            Log.w(TAG, "getTargetElement: not find")
        }
        return target
    }

    protected fun resetAll() {
        for (e in elements) {
            e.reset()
        }
    }

    private fun isParentNotVisible(parent: Element?): Boolean {
        if (parent == null) {
            return false
        }
        return if (parent.rect.left >= measuredWidth ||
            parent.rect.top >= measuredHeight
        ) {
            true
        } else {
            isParentNotVisible(parent.parentElement)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        elements.clear()
    }

    fun tryGetFrontView(targetActivity: Activity) {
        traverse(targetActivity.tryGetTheFrontView())
    }

    companion object {
        private const val TAG = "ElementHoldView"
    }
}
