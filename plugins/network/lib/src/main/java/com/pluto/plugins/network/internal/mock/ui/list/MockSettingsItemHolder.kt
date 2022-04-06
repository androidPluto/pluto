package com.pluto.plugins.network.internal.mock.ui.list

import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkItemMockSettingsBinding
import com.pluto.plugins.network.internal.mock.logic.dao.MockSettingsEntity

internal class MockSettingsItemHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_network___item_mock_settings), listener) {

    private val binding = PlutoNetworkItemMockSettingsBinding.bind(itemView)
    private val value = binding.value

    override fun onBind(item: ListItem) {
        if (item is MockSettingsEntity) {
            value.text = item.requestUrl
            binding.root.setDebounceClickListener {
                onAction("click")
            }
        }
    }
}
