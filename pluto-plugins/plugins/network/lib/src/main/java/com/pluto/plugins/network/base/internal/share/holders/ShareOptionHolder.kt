package com.pluto.plugins.network.base.internal.share.holders

import android.view.ViewGroup
import com.pluto.plugins.network.base.R
import com.pluto.plugins.network.base.databinding
.PlutoNetworkItemShareOptionBinding
import com.pluto.plugins.network.base.internal.share.ShareOptionType
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener

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
