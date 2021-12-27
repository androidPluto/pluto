package com.mocklets.pluto.plugin.options

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.databinding.PlutoItemPluginOptionBinding
import com.mocklets.pluto.plugin.PluginOption
import com.mocklets.pluto.plugin.utilities.extensions.inflate
import com.mocklets.pluto.plugin.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.plugin.utilities.list.DiffAwareHolder
import com.mocklets.pluto.plugin.utilities.list.ListItem
import com.mocklets.pluto.plugin.utilities.setDebounceClickListener

internal class PluginOptionItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_plugin_option), actionListener) {

    private val binding = PlutoItemPluginOptionBinding.bind(itemView)
    private val label = binding.label
    private val icon = binding.icon

    override fun onBind(item: ListItem) {
        if (item is PluginOption) {
            icon.setImageResource(item.icon)
            label.text = item.label
            binding.root.setDebounceClickListener {
                onAction("click")
            }
        }
    }
}
