package com.pluto.plugin.selector

import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoItemPluginGroupChildBinding
import com.pluto.plugin.Plugin
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener

internal class PluginGroupChildItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_plugin_group_child), actionListener) {

    private val binding = PlutoItemPluginGroupChildBinding.bind(itemView)
    private val name = binding.name
    private val icon = binding.icon

    override fun onBind(item: ListItem) {
        if (item is Plugin) {
            icon.setImageResource(item.getConfig().icon)
            name.text = item.getConfig().name

            binding.root.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
