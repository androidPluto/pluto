package com.pluto.utilities.selector.ui

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.selector.SelectorOption

internal class SelectorAdapter<T>(private val listener: OnActionListener, private val lifecycleOwner: LifecycleOwner, private val selected: T) : BaseAdapter() {

    override fun getItemViewType(item: ListItem): Int? {
        return when {
            lifecycleOwner is SelectorOption -> ITEM_TYPE_SINGLE
            lifecycleOwner.isListOf<SelectorOption>() -> ITEM_TYPE_MULTI
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_SINGLE -> SingleSelectorItemHolder(parent, listener, selected as SelectorOption)
            ITEM_TYPE_MULTI -> MultiSelectorItemHolder(parent, listener, arrayListOf<SelectorOption>().apply { addAll(selected as List<SelectorOption>) })
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_SINGLE = 1001
        const val ITEM_TYPE_MULTI = 1002
    }
}

inline fun <reified T> Any?.isListOf(): Boolean = this is List<*> && all { it is T }
