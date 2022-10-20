package com.pluto.plugins.rooms.db.internal.ui.list.table

import android.view.ViewGroup
import com.pluto.plugins.rooms.db.internal.TableModel
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class TableListAdapter(private val listener: OnActionListener) : BaseAdapter() {

    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is TableModel -> ITEM_TYPE_MODEL
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_MODEL -> TableListItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_MODEL = 1001
    }
}
