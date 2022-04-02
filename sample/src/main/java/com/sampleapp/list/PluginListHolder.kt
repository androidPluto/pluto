package com.sampleapp.list

import android.view.ViewGroup
import com.sampleapp.R
import com.sampleapp.databinding.ItemPluginListBinding
import com.sampleapp.utils.DiffAdapter
import com.sampleapp.utils.DiffAwareHolder
import com.sampleapp.utils.ListItem
import com.sampleapp.utils.inflate

internal class PluginListHolder(
    parent: ViewGroup,
    actionListener: DiffAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.item_plugin_list), actionListener) {

    private val binding = ItemPluginListBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is PluginListItem) {
            binding.title.text = item.title.lowercase().capitalize()
            binding.root.setOnClickListener {
                onAction("click")
            }
        }
    }
}
