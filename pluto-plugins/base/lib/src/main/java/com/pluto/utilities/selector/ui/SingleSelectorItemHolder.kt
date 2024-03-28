package com.pluto.utilities.selector.ui

import android.view.ViewGroup
import com.pluto.plugin.R
import com.pluto.plugin.databinding.PlutoSingleChoiceSelectorItemBinding
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.selector.SelectorOption
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan

internal class SingleSelectorItemHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener,
    private val selected: SelectorOption
) : DiffAwareHolder(parent.inflate(R.layout.pluto___single_choice_selector_item), listener) {

    private val binding = PlutoSingleChoiceSelectorItemBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is SelectorOption) {
            binding.title.setSpan {
                append(item.displayText())
            }
            binding.checkbox.isSelected = selected == item
            binding.root.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
