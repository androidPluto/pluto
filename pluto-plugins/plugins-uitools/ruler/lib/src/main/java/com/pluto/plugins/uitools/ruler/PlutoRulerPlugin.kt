package com.pluto.plugins.uitools.ruler

import androidx.fragment.app.Fragment
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
import com.pluto.plugins.uitools.BuildConfig
import com.pluto.plugins.uitools.R
import com.pluto.plugins.uitools.ruler.internal.RulerFragment

class PlutoRulerPlugin : Plugin("ruler") {
    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = context.getString(R.string.pluto_ruler___plugin_name),
        icon = R.drawable.pluto_ruler___ic_logo,
        version = BuildConfig.VERSION_NAME
    )

    override fun getView(): Fragment = RulerFragment()

    override fun getDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            website = "https://plutolib.com",
            vcsLink = "https://github.com/plutolib/pluto",
            twitter = "https://twitter.com/pluto_lib"
        )
    }

    override fun onPluginInstalled() {
    }

    override fun onPluginDataCleared() {
    }
}
