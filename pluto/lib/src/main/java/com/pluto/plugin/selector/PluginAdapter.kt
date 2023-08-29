package com.pluto.plugin.selector

import android.view.ViewGroup
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginGroup
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class PluginAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is Plugin -> ITEM_TYPE_PLUGIN
            is PluginGroup -> ITEM_TYPE_PLUGIN_GROUP
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_PLUGIN -> PluginItemHolder(parent, listener)
            ITEM_TYPE_PLUGIN_GROUP -> PluginGroupItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_PLUGIN = 1000
        const val ITEM_TYPE_PLUGIN_GROUP = 1001
    }
}
