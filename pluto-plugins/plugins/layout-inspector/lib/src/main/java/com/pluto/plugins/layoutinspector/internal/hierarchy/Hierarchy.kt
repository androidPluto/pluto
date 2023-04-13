package com.pluto.plugins.layoutinspector.internal.hierarchy

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.pluto.plugins.layoutinspector.internal.inspect.verifyTargetTag
import com.pluto.utilities.list.ListItem

class Hierarchy(
    val view: View,
    val layerCount: Int,
    var isExpanded: Boolean = false
) : ListItem() {
    override fun isEqual(other: Any): Boolean {
        return other is Hierarchy && other.view.javaClass.name == view.javaClass.name && other.isExpanded == isExpanded
    }

    val isTargetView: Boolean
        get() = view.verifyTargetTag()

    fun assembleChildren(recursive: Boolean = false): List<Hierarchy> {
        val result = arrayListOf<Hierarchy>()
        if (view is ViewGroup) {
            view.children.forEach {
                val item = Hierarchy(it, layerCount + 1, isExpanded = recursive)
                result.add(item)
                if (recursive) {
                    result.addAll(item.assembleChildren(true))
                }
            }
        }
        return result
    }
}
