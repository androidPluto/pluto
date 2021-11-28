package com.mocklets.pluto.ui

import android.content.Context
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.databinding.PlutoActivityPlutoBinding
import com.mocklets.pluto.plugin.Plugin
import com.mocklets.pluto.utilities.list.BaseAdapter
import com.mocklets.pluto.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.utilities.list.DiffAwareHolder
import com.mocklets.pluto.utilities.list.ListItem
import com.mocklets.pluto.utilities.setDebounceClickListener

class NavigationDrawerManager(private val context: Context, private val binding: PlutoActivityPlutoBinding, private val onSelection: (Plugin) -> Unit) {

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is Plugin) {
                context.let {
                    onSelection.invoke(data)
                }
            }
        }
    }

    fun init() {
        val pluginAdapter: BaseAdapter = PluginAdapter(onActionListener)
        binding.nvView.pluginList.adapter = pluginAdapter
        pluginAdapter.list = Pluto.pluginManager.installedPlugins


        //todo testing code START
        binding.nvView.settings.text = Pluto.pluginManager.installedPlugins[0].getConfig().name
        binding.nvView.about.text = Pluto.pluginManager.installedPlugins[1].getConfig().name

        binding.nvView.settings.setDebounceClickListener {
            onSelection.invoke(Pluto.pluginManager.installedPlugins[0])
        }

        binding.nvView.about.setDebounceClickListener {
            onSelection.invoke(Pluto.pluginManager.installedPlugins[1])
        }
        //todo testing code END
    }
}
