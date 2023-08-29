package com.pluto.plugins.network.base.internal.share

import android.view.ViewGroup
import com.pluto.plugins.network.base.internal.share.holders.ShareHeadingHolder
import com.pluto.plugins.network.base.internal.share.holders.ShareOptionHolder
import com.pluto.plugins.network.base.internal.share.holders.ShareResponseOptionHolder
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem

internal class ShareOptionsAdapter(private val listener: OnActionListener) : BaseAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is ShareOptionType.All -> ITEM_TYPE_OPTION
            is ShareOptionType.CURL -> ITEM_TYPE_OPTION
            is ShareOptionType.Request -> ITEM_TYPE_OPTION
            is ShareOptionType.Response -> ITEM_TYPE_OPTION_RESPONSE
            is ShareOptionType.Header -> ITEM_TYPE_HEADER
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_OPTION -> ShareOptionHolder(parent, listener)
            ITEM_TYPE_OPTION_RESPONSE -> ShareResponseOptionHolder(parent, listener)
            ITEM_TYPE_HEADER -> ShareHeadingHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_OPTION = 1000
        const val ITEM_TYPE_OPTION_RESPONSE = 1001
        const val ITEM_TYPE_HEADER = 1002
    }
}
