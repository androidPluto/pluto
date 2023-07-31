package com.pluto.plugins.network.base.internal.mock.ui.list

import android.view.ViewGroup
import com.pluto.plugins.network.base.R
import com.pluto.plugins.network.base.databinding
.PlutoNetworkItemMockSettingsBinding
import com.pluto.plugins.network.base.internal.mock.logic.dao.MockSettingsEntity
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener

internal class MockSettingsItemHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_network___item_mock_settings), listener) {

    private val binding = PlutoNetworkItemMockSettingsBinding.bind(itemView)
    private val value = binding.value

    override fun onBind(item: ListItem) {
        if (item is MockSettingsEntity) {
            value.text = item.requestUrl
            binding.root.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
