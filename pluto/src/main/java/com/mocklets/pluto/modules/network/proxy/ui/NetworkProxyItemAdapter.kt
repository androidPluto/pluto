package com.mocklets.pluto.modules.network.proxy.ui

import android.view.ViewGroup
import com.mocklets.pluto.core.ui.list.BaseAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.modules.network.proxy.dao.NetworkProxyEntity

internal class NetworkProxyItemAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is NetworkProxyEntity -> ITEM_TYPE_NETWORK_PROXY
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_NETWORK_PROXY -> NetworkProxyItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_NETWORK_PROXY = 1000
    }
}
