package com.pluto.ui.selector.options

import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoItemPluginOptionBinding
import com.pluto.plugin.PluginOption
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setOnDebounceClickListener

internal class PluginOptionItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_plugin_option), actionListener) {

    private val binding = PlutoItemPluginOptionBinding.bind(itemView)
    private val label = binding.label
    private val icon = binding.icon

    override fun onBind(item: ListItem) {
        if (item is PluginOption) {
            icon.setImageResource(item.icon)
            label.text = item.label
            binding.root.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
