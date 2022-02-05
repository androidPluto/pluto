package com.pluto.plugin.list

import android.view.ViewGroup
import com.pluto.plugin.Plugin
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem

internal class PluginAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is Plugin -> ITEM_TYPE_PLUGIN
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_PLUGIN -> PluginItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_PLUGIN = 1000
    }
}
