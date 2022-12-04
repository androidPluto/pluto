package com.pluto.plugins.ruler

import androidx.fragment.app.Fragment
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
import com.pluto.plugins.ruler.com.pluto.plugins.ruler.internal.RulerFragment

class PlutoRulerPlugin(devIdentifier: String) : Plugin(devIdentifier) {
    override fun getConfig() = PluginConfiguration(
        name = context.getString(R.string.pluto_ruler___plugin_name),
        icon = R.drawable.pluto_ruler___ic_plugin_logo,
        version = BuildConfig.VERSION_NAME
    )

    override fun getView(): Fragment = RulerFragment()

    override fun onPluginInstalled() {
    }

    override fun onPluginDataCleared() {
    }
}
