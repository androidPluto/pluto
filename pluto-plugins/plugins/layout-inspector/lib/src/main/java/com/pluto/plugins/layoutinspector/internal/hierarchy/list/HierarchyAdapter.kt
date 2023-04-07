package com.pluto.plugins.layoutinspector.internal.hierarchy.list

import android.view.ViewGroup
import com.pluto.plugins.layoutinspector.internal.hierarchy.Hierarchy
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class HierarchyAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is Hierarchy -> ITEM_TYPE_HIERARCHY
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_HIERARCHY -> HierarchyItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_HIERARCHY = 1000
    }
}
