package com.pluto.plugins.preferences

import androidx.fragment.app.Fragment
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration

class PlutoSharePreferencesPlugin(identifier: String) : Plugin(identifier) {

    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = context.getString(R.string.pluto_pref___plugin_name),
        icon = R.drawable.pluto_pref___ic_pref_icon,
        version = BuildConfig.VERSION_NAME
    )

    override fun getView(): Fragment = SharedPrefFragment()

    override fun getDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            website = "https://plutolib.com",
            vcsLink = "https://github.com/plutolib/plugin-shared-preferences",
            twitter = "https://twitter.com/pluto_lib"
        )
    }

    override fun onPluginInstalled() {
        SharedPrefRepo.init(context)
    }

    override fun onPluginDataCleared() {
    }
}
