package com.pluto.plugins.exceptions.internal.ui

import android.view.ViewGroup
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugins.exceptions.internal.ProcessThread
import com.pluto.plugins.exceptions.internal.ui.holder.StackTraceListItemHolder

internal class StackTracesAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is ProcessThread -> ITEM_TYPE_THREAD_STACK_TRACE
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_THREAD_STACK_TRACE -> StackTraceListItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_THREAD_STACK_TRACE = 1201
    }
}
