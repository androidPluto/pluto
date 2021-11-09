package com.mocklets.pluto.network.ui.proxy

import android.view.ViewGroup
import com.mocklets.pluto.network.R
import com.mocklets.pluto.network.databinding.PlutoNetworkItemNetworkProxySettingsBinding
import com.mocklets.pluto.network.internal.proxy.dao.NetworkProxyEntity
import com.mocklets.pluto.utilities.extensions.inflate
import com.mocklets.pluto.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.utilities.list.DiffAwareHolder
import com.mocklets.pluto.utilities.list.ListItem
import com.mocklets.pluto.utilities.setDebounceClickListener

internal class NetworkProxyItemHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_network___item_network_proxy_settings), listener) {

    private val binding = PlutoNetworkItemNetworkProxySettingsBinding.bind(itemView)
    private val value = binding.value

    override fun onBind(item: ListItem) {
        if (item is NetworkProxyEntity) {
            value.text = item.requestUrl
            binding.root.setDebounceClickListener {
                onAction("click")
            }
        }
    }
}
