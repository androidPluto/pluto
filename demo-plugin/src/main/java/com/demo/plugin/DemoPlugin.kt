package com.demo.plugin

import androidx.fragment.app.Fragment
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
import com.pluto.plugin.utilities.DebugLog

class DemoPlugin(devIdentifier: String) : Plugin(devIdentifier) {

    init {
        Session.devIdentifier = devIdentifier
    }

    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = context.getString(R.string.demo___plugin_name),
        icon = R.drawable.demo___ic_plugin_icon
    )

    override fun getView(): Fragment = DemoFragment()

    override fun onPluginInstalled() {
        DebugLog.d("demo_plugin", "$devIdentifier installed")
    }

    override fun onPluginDataCleared() {
    }
}
