package com.pluto.preferences.ui

import android.view.ViewGroup
import com.mocklets.pluto.plugin.utilities.list.BaseAdapter
import com.mocklets.pluto.plugin.utilities.list.DiffAwareHolder
import com.mocklets.pluto.plugin.utilities.list.ListItem
import com.pluto.preferences.ui.filter.SharedPrefFilterItemHolder

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
            ITEM_TYPE_PAIR -> SharedPrefKeyValueItemHolder(parent, listener)
            ITEM_TYPE_FILTER -> SharedPrefFilterItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_PAIR = 1001
        const val ITEM_TYPE_FILTER = 1002
    }
}
