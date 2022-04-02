package com.sampleapp.list

import android.view.ViewGroup
import androidx.annotation.Keep
import com.sampleapp.utils.DiffAwareHolder
import com.sampleapp.utils.ListAdapter
import com.sampleapp.utils.ListItem

internal class PluginListAdapter(private val listener: OnActionListener) : ListAdapter() {
    override fun getItemViewType(item: ListItem): Int? {
        return when (item) {
            is PluginListItem -> ITEM_TYPE_PLUGIN
            else -> null
        }
    }

    override fun onViewHolderCreated(parent: ViewGroup, viewType: Int): DiffAwareHolder? {
        return when (viewType) {
            ITEM_TYPE_PLUGIN -> PluginListHolder(parent, listener)
            else -> null
        }
    }

    companion object {
        const val ITEM_TYPE_PLUGIN = 1000
    }
}

@Keep
data class PluginListItem(val title: String) : ListItem()
