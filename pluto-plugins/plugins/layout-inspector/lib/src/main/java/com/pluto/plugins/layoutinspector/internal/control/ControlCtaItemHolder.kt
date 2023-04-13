package com.pluto.plugins.layoutinspector.internal.control

import android.view.ViewGroup
import com.pluto.plugins.layoutinspector.R
import com.pluto.plugins.layoutinspector.databinding.PlutoLiItemControlCtaBinding
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener

internal class ControlCtaItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto_li___item_control_cta), actionListener) {

    private val binding = PlutoLiItemControlCtaBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is ControlCta) {
            binding.icon.setImageResource(item.icon)
            binding.root.setOnDebounceClickListener {
                onAction(item.id)
            }
            binding.root.setOnLongClickListener {
                context.toast(item.hint)
                true
            }
        }
    }
}
