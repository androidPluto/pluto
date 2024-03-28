package com.pluto.utilities.selector.ui

import android.view.ViewGroup
import com.pluto.plugin.R
import com.pluto.plugin.databinding.PlutoMultiChoiceSelectorItemBinding
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.selector.SelectorOption
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan

internal class MultiSelectorItemHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener,
    private val selected: List<SelectorOption>
) : DiffAwareHolder(parent.inflate(R.layout.pluto___multi_choice_selector_item), listener) {

    private val binding = PlutoMultiChoiceSelectorItemBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is SelectorOption) {
            binding.title.setSpan {
                append(item.displayText())
            }
            binding.checkbox.isSelected = selected.contains(item)
            binding.root.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
