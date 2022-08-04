package com.pluto.plugins.network.internal.interceptor.ui.share.holders

import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkItemShareOptionBinding
import com.pluto.plugins.network.internal.interceptor.ui.share.ShareOptionType

internal class ShareOptionHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto_network___item_share_option), actionListener) {

    private val binding = PlutoNetworkItemShareOptionBinding.bind(itemView)
    override fun onBind(item: ListItem) {
        if (item is ShareOptionType) {
            binding.label.text = item.title
            binding.label.setCompoundDrawablesWithIntrinsicBounds(item.icon, 0, 0, 0)
            binding.root.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
