package com.mocklets.pluto.modules.appstate

import android.view.ViewGroup
import com.mocklets.pluto.core.ui.list.BaseAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem

internal class AppStateItemAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is AppStateItem -> ITEM_TYPE_APP_STATE
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_APP_STATE -> AppStateItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_APP_STATE = 1000
    }
}
