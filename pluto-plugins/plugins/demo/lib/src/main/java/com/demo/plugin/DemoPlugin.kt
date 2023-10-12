package com.demo.plugin

import androidx.fragment.app.Fragment
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
import com.pluto.utilities.DebugLog

class DemoPlugin() : Plugin(ID) {

    @SuppressWarnings("UnusedPrivateMember")
    @Deprecated("Use the default constructor DemoPlugin() instead.")
    constructor(identifier: String) : this()

    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = context.getString(R.string.demo___plugin_name),
        icon = R.drawable.demo___ic_plugin_icon,
        version = BuildConfig.VERSION_NAME
    )

    override fun getDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            website = "https://androidpluto.com",
            vcsLink = "https://github.com/androidPluto/pluto",
            twitter = "https://twitter.com/android_pluto"
        )
    }

    override fun getView(): Fragment = DemoFragment()

    override fun onPluginInstalled() {
        DebugLog.d("demo_plugin", "$ID installed")
    }

    override fun onPluginDataCleared() {
    }

    companion object {
        const val ID = "demo"
    }
}
