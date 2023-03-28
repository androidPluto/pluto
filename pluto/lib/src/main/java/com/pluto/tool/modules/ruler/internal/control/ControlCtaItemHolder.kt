package com.pluto.tool.modules.ruler.internal.control

import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoToolItemControlCtaBinding
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener

internal class ControlCtaItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___tool_item_control_cta), actionListener) {

    private val binding = PlutoToolItemControlCtaBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is ControlCta) {
            binding.icon.setImageResource(item.icon)
            binding.root.setOnDebounceClickListener {
                onAction(item.id)
            }
            binding.root.setOnLongClickListener {
                context.toast(item.id)
                true
            }
        }
    }
}
