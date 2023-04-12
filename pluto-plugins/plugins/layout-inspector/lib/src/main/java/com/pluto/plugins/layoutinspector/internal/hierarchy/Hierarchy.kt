package com.pluto.plugins.layoutinspector.internal.hierarchy

import android.view.View
import android.view.ViewGroup
import com.pluto.plugins.layoutinspector.internal.inspect.verifyTargetTag
import com.pluto.utilities.list.ListItem

class Hierarchy(
    val view: View,
    val layerCount: Int,
    var isExpanded: Boolean = false,
    var sysLayerCount: Int = 0,
) : ListItem() {
    override fun isEqual(other: Any): Boolean {
        return other is Hierarchy && other.view.javaClass.name == view.javaClass.name && other.isExpanded == isExpanded
    }

    val isTargetView: Boolean
        get() = view.verifyTargetTag()

    fun assembleChildren(recursive: Boolean = false): List<Hierarchy> {
        val result = arrayListOf<Hierarchy>()
        if (view is ViewGroup) {
            val newLayerCount = layerCount + 1
            for (i in 0 until view.childCount) {
                val item = Hierarchy(view.getChildAt(i), newLayerCount, isExpanded = recursive)
                item.sysLayerCount = sysLayerCount
                result.add(item)
                if (recursive) {
                    result.addAll(item.assembleChildren(true))
                }
            }
        }
        return result
    }
}
