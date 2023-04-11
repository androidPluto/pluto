package com.pluto.plugins.layoutinspector.internal.hierarchy

import android.view.View
import android.view.ViewGroup
import com.pluto.plugins.layoutinspector.R
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
        get() = view.getTag(R.id.pluto_li___unique_view_tag) != null

    fun assembleChildren(): List<Hierarchy> {
        val result = arrayListOf<Hierarchy>()
        if (view is ViewGroup) {
            val newLayerCount = layerCount + 1
            for (i in 0 until view.childCount) {
                val item = Hierarchy(view.getChildAt(i), newLayerCount)
                item.sysLayerCount = sysLayerCount
                result.add(item)
            }
        }
        return result
    }
}
