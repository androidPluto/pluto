package com.pluto.plugins.options

import android.view.ViewGroup
import com.pluto.plugins.PluginOption
import com.pluto.plugins.utilities.list.BaseAdapter
import com.pluto.plugins.utilities.list.DiffAwareHolder
import com.pluto.plugins.utilities.list.ListItem

internal class PluginOptionAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is PluginOption -> ITEM_TYPE_PLUGIN_OPTION
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_PLUGIN_OPTION -> PluginOptionItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_PLUGIN_OPTION = 1000
    }
}
