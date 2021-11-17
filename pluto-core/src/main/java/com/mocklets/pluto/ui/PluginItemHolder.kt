package com.mocklets.pluto.ui

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.databinding.PlutoItemPluginBinding
import com.mocklets.pluto.utilities.extensions.dp
import com.mocklets.pluto.utilities.extensions.inflate
import com.mocklets.pluto.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.utilities.list.DiffAwareHolder
import com.mocklets.pluto.utilities.list.ListItem

internal class PluginItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_plugin), actionListener) {

    private val binding = PlutoItemPluginBinding.bind(itemView)
    private val logTag = binding.logtag
//    private val message = binding.message
//    private val timestamp = binding.timestamp

    override fun onBind(item: ListItem) {
        logTag.text = "Plugin name"
    }

    private companion object {
        val DRAWABLE_PADDING = 4f.dp.toInt()
    }
}
