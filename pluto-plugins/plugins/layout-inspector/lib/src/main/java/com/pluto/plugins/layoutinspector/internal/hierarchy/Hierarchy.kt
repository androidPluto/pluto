package com.pluto.plugins.layoutinspector.internal.hierarchy

import android.view.View
import android.view.ViewGroup
import com.pluto.utilities.list.ListItem

class Hierarchy(
    val view: View,
    val layerCount: Int,
    val isTarget: Boolean = false,
    val isExpanded: Boolean = false,
    val sysLayerCount: Int = 0,
) : ListItem() {
    val isGroup: Boolean
        get() = view is ViewGroup
    val childCount: Int
        get() = if (view is ViewGroup) view.childCount else 0
    val isVisible: Boolean
        get() = view.visibility == View.VISIBLE
}
