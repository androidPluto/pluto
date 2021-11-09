package com.mocklets.pluto.network.ui.proxy

import android.view.ViewGroup
import com.mocklets.pluto.network.internal.proxy.dao.NetworkProxyEntity
import com.mocklets.pluto.utilities.list.BaseAdapter
import com.mocklets.pluto.utilities.list.DiffAwareHolder
import com.mocklets.pluto.utilities.list.ListItem

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
