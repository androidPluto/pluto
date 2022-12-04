package com.pluto.plugins.ruler.com.pluto.plugins.ruler.internal.control

import android.view.ViewGroup
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class ControlCtaAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is ControlCta -> ITEM_TYPE_CONTROL_CTA
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_CONTROL_CTA -> ControlCtaItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_CONTROL_CTA = 1000
    }
}
