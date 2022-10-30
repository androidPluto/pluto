package com.pluto.plugins.ruler.com.pluto.plugins.ruler.internal.hint

import android.view.ViewGroup
import com.pluto.plugins.ruler.R
import com.pluto.plugins.ruler.databinding.PlutoRulerItemHintBinding
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class HintItemHolder(parent: ViewGroup, listener: DiffAwareAdapter.OnActionListener? = null) :
    DiffAwareHolder(parent.inflate(R.layout.pluto_ruler___item_hint), listener) {

    private val binding = PlutoRulerItemHintBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is HintItem) {
            binding.text.text = "${layoutPosition + 1}.\t${item.text}"
        }
    }
}
