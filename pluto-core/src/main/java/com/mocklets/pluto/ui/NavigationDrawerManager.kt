package com.mocklets.pluto.ui

import android.content.Context
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.databinding.PlutoActivityPlutoBinding
import com.mocklets.pluto.plugin.Plugin
import com.mocklets.pluto.utilities.list.BaseAdapter
import com.mocklets.pluto.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.utilities.list.DiffAwareHolder
import com.mocklets.pluto.utilities.list.ListItem

class NavigationDrawerManager(context: Context, private val binding: PlutoActivityPlutoBinding, onSelection: (Plugin) -> Unit) {

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is Plugin) {
                context.let {
                    onSelection.invoke(data)
                }
            }
        }
    }

    private val pluginAdapter: BaseAdapter = PluginAdapter(onActionListener)

    fun init() {
        binding.nvView.pluginList.adapter = pluginAdapter
        pluginAdapter.list = Pluto.pluginManager.installedPlugins
    }
}
