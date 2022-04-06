package com.pluto.plugins.preferences.ui

import android.view.ViewGroup
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugins.preferences.ui.filter.FilterItemHolder

internal class SharedPrefAdapter(private val listener: OnActionListener) : BaseAdapter() {

    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is SharedPrefKeyValuePair -> ITEM_TYPE_PAIR
            is SharedPrefFile -> ITEM_TYPE_FILTER
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_PAIR -> KeyValueItemHolder(parent, listener)
            ITEM_TYPE_FILTER -> FilterItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_PAIR = 1001
        const val ITEM_TYPE_FILTER = 1002
    }
}
