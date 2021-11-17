package com.mocklets.pluto.ui

import android.content.Context
import com.mocklets.pluto.databinding.PlutoActivityPlutoBinding
import com.mocklets.pluto.utilities.list.BaseAdapter
import com.mocklets.pluto.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.utilities.list.DiffAwareHolder
import com.mocklets.pluto.utilities.list.ListItem

class NavigationDrawerManager(context: Context, private val binding: PlutoActivityPlutoBinding, onSelection: (Any) -> Unit) {
    private val pluginAdapter: BaseAdapter by lazy { PluginAdapter(onActionListener) }

    init {
//        pluginAdapter.list = Pluto.pluginManager.installedPlugins
        binding.nvView.pluginList.adapter = pluginAdapter
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            context.let {
                onSelection.invoke(Any())
            }
        }
    }
}
