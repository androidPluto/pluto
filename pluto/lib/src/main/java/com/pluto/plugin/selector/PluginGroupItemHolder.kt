package com.pluto.plugin.selector

import android.view.ViewGroup
import com.pluto.R
import com.pluto.databinding.PlutoItemPluginGroupBinding
import com.pluto.plugin.PluginGroup
import com.pluto.ui.ListWrapper
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener

internal class PluginGroupItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_plugin_group), actionListener) {

    private val binding = PlutoItemPluginGroupBinding.bind(itemView)
    private val name = binding.name
    private val icon = binding.icon

    override fun onBind(item: ListItem) {
        if (item is ListWrapper<*> && item.get() is PluginGroup) {
            val group = item.get() as PluginGroup
            icon.setImageResource(group.getConfig().icon)
            name.text = group.getConfig().name
            binding.root.setOnDebounceClickListener {
                onAction("click")
            }
        }
    }
}
