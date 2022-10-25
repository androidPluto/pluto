package com.pluto.plugins.rooms.db.internal.ui.list.database

import android.view.ViewGroup
import com.pluto.plugins.rooms.db.internal.DatabaseModel
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class DBListAdapter(private val listener: OnActionListener) : BaseAdapter() {

    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is DatabaseModel -> ITEM_TYPE_DB
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_DB -> DBListItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_DB = 1001
    }
}
