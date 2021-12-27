package com.mocklets.pluto.network.ui

import android.view.ViewGroup
import com.mocklets.pluto.network.internal.ApiCallData
import com.mocklets.pluto.plugin.utilities.list.BaseAdapter
import com.mocklets.pluto.plugin.utilities.list.DiffAwareHolder
import com.mocklets.pluto.plugin.utilities.list.ListItem

internal class NetworkAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is ApiCallData -> ITEM_TYPE_API
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_API -> ApiItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_API = 1000
    }
}
