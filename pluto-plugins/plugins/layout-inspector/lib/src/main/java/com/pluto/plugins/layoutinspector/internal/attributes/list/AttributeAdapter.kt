package com.pluto.plugins.layoutinspector.internal.attributes.list

import android.view.ViewGroup
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class AttributeAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is Attribute -> ITEM_TYPE_ATTRIBUTE
            is AttributeTitle -> ITEM_TYPE_TITLE
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_ATTRIBUTE -> AttributeItemHolder(parent, listener)
            ITEM_TYPE_TITLE -> AttributeTitleItemHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_ATTRIBUTE = 1000
        const val ITEM_TYPE_TITLE = 1001
    }
}
