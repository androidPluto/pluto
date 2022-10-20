package com.pluto.tools.modules.ruler.internal.hint

import android.view.ViewGroup
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class HintAdapter : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is HintItem -> ITEM_TYPE_HINT
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_HINT -> HintItemHolder(parent)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_HINT = 1000
    }
}

data class HintItem(
    val text: String
) : ListItem()
