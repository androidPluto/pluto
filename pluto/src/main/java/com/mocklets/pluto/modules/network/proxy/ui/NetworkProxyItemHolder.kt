package com.mocklets.pluto.modules.network.proxy.ui

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.databinding.PlutoItemNetworkProxySettingsBinding
import com.mocklets.pluto.modules.network.proxy.dao.NetworkProxyEntity

internal class NetworkProxyItemHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_network_proxy_settings), listener) {

    private val binding = PlutoItemNetworkProxySettingsBinding.bind(itemView)
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
