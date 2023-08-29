package com.pluto.plugins.network.internal.share.holders

import android.content.res.ColorStateList
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.pluto.plugins.network.base.R
import com.pluto.plugins.network.base.databinding
    .PlutoNetworkItemShareOptionResponseBinding
import com.pluto.plugins.network.internal.share.ShareOptionType
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan

internal class ShareResponseOptionHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto_network___item_share_option_response), actionListener) {

    private val binding = PlutoNetworkItemShareOptionResponseBinding.bind(itemView)
    override fun onBind(item: ListItem) {
        if (item is ShareOptionType) {
            binding.label.setCompoundDrawablesWithIntrinsicBounds(item.icon, 0, 0, 0)
            if (item.enabled) {
                binding.root.setOnDebounceClickListener {
                    onAction("click")
                }
                binding.label.setSpan {
                    append(fontColor(item.title, context.color(R.color.pluto___text_dark_80)))
                }
                binding.description.visibility = GONE
                binding.waiting.visibility = GONE
                binding.root.setBackgroundColor(context.color(R.color.pluto___transparent))
            } else {
                binding.label.setSpan {
                    append(fontColor(item.title, context.color(R.color.pluto___text_dark_40)))
                }
                binding.description.text = item.subtitle
                binding.description.visibility = VISIBLE
                binding.waiting.visibility = VISIBLE
                binding.waiting.indeterminateTintList = ColorStateList.valueOf(context.color(R.color.pluto___dark_20))
                binding.root.setBackgroundColor(context.color(R.color.pluto___app_bg))
            }
        }
    }
}
