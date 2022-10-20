package com.pluto.plugins.logger.internal.ui.list

import android.view.ViewGroup
import com.pluto.plugins.logger.internal.LogData
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

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
