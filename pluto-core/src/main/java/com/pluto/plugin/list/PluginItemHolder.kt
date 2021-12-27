package com.pluto.plugin.list

import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoItemPluginBinding
import com.pluto.plugin.Plugin
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setDebounceClickListener

internal class PluginItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_plugin), actionListener) {

    private val binding = PlutoItemPluginBinding.bind(itemView)
    private val name = binding.name
    private val icon = binding.icon

    override fun onBind(item: ListItem) {
        if (item is Plugin) {
            icon.setImageResource(item.getConfig().icon)
            name.text = item.getConfig().name
            binding.root.setDebounceClickListener {
                onAction("click")
            }
        }
    }
}
