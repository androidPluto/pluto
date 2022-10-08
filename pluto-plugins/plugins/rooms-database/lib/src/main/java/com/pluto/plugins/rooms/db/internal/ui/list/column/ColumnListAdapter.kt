package com.pluto.plugins.rooms.db.internal.ui.list.column

import android.view.ViewGroup
import com.pluto.plugins.rooms.db.internal.ColumnModel
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class ColumnListAdapter : BaseAdapter() {

    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is ColumnModel -> ITEM_TYPE_MODEL
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_MODEL -> ColumnListItemHolder(parent, null)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_MODEL = 1001
    }
}
