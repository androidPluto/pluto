package com.pluto.ui.selector

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.pluto.Pluto
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginGroup
import com.pluto.tool.PlutoTool
import com.pluto.ui.ListWrapper
import com.pluto.utilities.list.ListItem

internal class SelectorUtils {

    private val containerActivity: FragmentActivity
    private lateinit var pluginsGroupViewModel: PluginsGroupViewModel

    constructor(activity: FragmentActivity) {
        containerActivity = activity
        init()
    }

    constructor(fragment: Fragment) {
        containerActivity = fragment.requireActivity()
        init()
    }

    private fun init() {
        pluginsGroupViewModel = ViewModelProvider(
            containerActivity,
            ViewModelProvider.AndroidViewModelFactory(containerActivity.application)
        ).get(PluginsGroupViewModel::class.java)
    }

    fun onSelect(action: String, data: ListItem) {
        when {
            data is ListWrapper<*> && data.get() is Plugin -> {
                val plugin = data.get() as Plugin
                when (action) {
                    "click" -> {
                        Pluto.open(plugin.identifier)
                        containerActivity.finish()
                    }

                    "long_click" -> {
                        val devDetailsFragment = DevDetailsFragment()
                        devDetailsFragment.arguments = Bundle().apply {
                            putString("name", plugin.getConfig().name)
                            putInt("icon", plugin.getConfig().icon)
                            putString("version", plugin.getConfig().version)
                            putString("website", plugin.getDeveloperDetails()?.website)
                            putString("vcs", plugin.getDeveloperDetails()?.vcsLink)
                            putString("twitter", plugin.getDeveloperDetails()?.twitter)
                        }
                        devDetailsFragment.show(containerActivity.supportFragmentManager, "devDetails")
                    }
                }
            }

            data is ListWrapper<*> && data.get() is PluginGroup -> {
                pluginsGroupViewModel.set(data.get() as PluginGroup)
                val groupSelectorFragment = GroupSelectorFragment()
                groupSelectorFragment.show(containerActivity.supportFragmentManager, "groupSelector")
            }

            data is PlutoTool -> {
                Pluto.toolManager.select(data.id)
                containerActivity.finish()
            }
        }
    }
}
