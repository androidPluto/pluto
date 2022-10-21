package com.pluto.tool.modules.ruler.internal.hint

import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoToolItemHintBinding
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class HintItemHolder(parent: ViewGroup, listener: DiffAwareAdapter.OnActionListener? = null) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___tool_item_hint), listener) {

    private val binding = PlutoToolItemHintBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is HintItem) {
            binding.text.text = "${layoutPosition + 1}.\t${item.text}"
        }
    }
}
