package com.pluto.plugins.options

import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoItemPluginOptionBinding
import com.pluto.plugins.PluginOption
import com.pluto.plugins.utilities.extensions.inflate
import com.pluto.plugins.utilities.list.DiffAwareAdapter
import com.pluto.plugins.utilities.list.DiffAwareHolder
import com.pluto.plugins.utilities.list.ListItem
import com.pluto.plugins.utilities.setDebounceClickListener

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
