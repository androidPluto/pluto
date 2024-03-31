package com.pluto.plugins.datastore.pref.ui

import android.view.ViewGroup
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class DatastorePrefAdapter(private val listener: OnActionListener) : BaseAdapter() {

    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is DatastorePrefKeyValuePair -> ITEM_TYPE_PAIR
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_PAIR -> KeyValueItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_PAIR = 1001
    }
}
