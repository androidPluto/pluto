package com.mocklets.pluto.modules.customactions.ui

import android.view.ViewGroup
import com.mocklets.pluto.core.ui.list.BaseAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.modules.customactions.CustomAction

internal class CustomActionAdapter(
    private val listener: OnActionListener
) : BaseAdapter() {

    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is CustomAction -> ITEM_TYPE_CUSTOM_ACTION
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_CUSTOM_ACTION -> CustomActionItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_CUSTOM_ACTION = 1000
    }
}
