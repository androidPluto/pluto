package com.pluto.utilities.selector.ui

import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.pluto.plugin.R
import com.pluto.plugin.databinding.PlutoMultiChoiceSelectorItemBinding
import com.pluto.utilities.SingleLiveEvent
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.selector.SelectorOption
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan

internal class MultiSelectorAdapter(
    private val listener: OnActionListener,
    private val selectedLiveData: SingleLiveEvent<List<SelectorOption>>
) : BaseAdapter() {

    override fun getItemViewType(item: ListItem): Int = 1

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int) = MultiSelectorItemHolder(parent, listener, selectedLiveData)
}

internal class MultiSelectorItemHolder(
    parent: ViewGroup,
    listener: DiffAwareAdapter.OnActionListener,
    private val selectedLiveData: SingleLiveEvent<List<SelectorOption>>
) : DiffAwareHolder(parent.inflate(R.layout.pluto___multi_choice_selector_item), listener) {

    private val binding = PlutoMultiChoiceSelectorItemBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is SelectorOption) {
            binding.title.setSpan {
                append(item.displayText())
            }

            binding.root.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }

    override fun onAttachViewHolder() {
        super.onAttachViewHolder()
        selectedLiveData.observeForever(selectedChoicesObserver)
    }

    override fun onDetachViewHolder() {
        super.onDetachViewHolder()
        selectedLiveData.removeObserver(selectedChoicesObserver)
    }

    private val selectedChoicesObserver = Observer<List<SelectorOption>> {
        binding.checkbox.isSelected = it?.contains(item) ?: run { false }
    }
}
