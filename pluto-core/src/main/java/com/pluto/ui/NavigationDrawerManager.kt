package com.pluto.ui

import android.content.Context
import com.pluto.Pluto
import com.pluto.databinding.PlutoActivityPlutoBinding
import com.pluto.plugin.Plugin
import com.pluto.plugin.list.PluginAdapter
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem

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

//        // todo testing code START
//        binding.nvView.settings.text = Pluto.pluginManager.installedPlugins[0].getConfig().name
//        binding.nvView.about.text = Pluto.pluginManager.installedPlugins[1].getConfig().name
//
//        binding.nvView.settings.setDebounceClickListener {
//            onSelection.invoke(Pluto.pluginManager.installedPlugins[0])
//        }
//
//        binding.nvView.about.setDebounceClickListener {
//            onSelection.invoke(Pluto.pluginManager.installedPlugins[1])
//        }
        // todo testing code END
    }
}
