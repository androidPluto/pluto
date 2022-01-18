package com.mocklets.pluto.modules.logging.ui

import android.view.ViewGroup
import com.mocklets.pluto.core.ui.list.BaseAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.modules.logging.LogData

internal class LogsAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is LogData -> ITEM_TYPE_LOG
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_LOG -> LogItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_LOG = 1000
    }
}
