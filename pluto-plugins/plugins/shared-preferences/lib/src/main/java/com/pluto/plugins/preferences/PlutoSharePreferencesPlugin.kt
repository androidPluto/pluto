package com.pluto.plugins.preferences

import androidx.fragment.app.Fragment
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration

class PlutoSharePreferencesPlugin() : Plugin(ID) {

    @SuppressWarnings("UnusedPrivateMember")
    @Deprecated("Use the default constructor PlutoSharePreferencesPlugin() instead.")
    constructor(identifier: String) : this()

    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = context.getString(R.string.pluto_pref___plugin_name),
        icon = R.drawable.pluto_pref___ic_pref_icon,
        version = BuildConfig.VERSION_NAME
    )

    override fun getView(): Fragment = SharedPrefFragment()

    override fun getDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            website = "https://androidpluto.com",
            vcsLink = "https://github.com/androidPluto/pluto",
            twitter = "https://twitter.com/android_pluto"
        )
    }

    override fun onPluginInstalled() {
        SharedPrefRepo.init(context)
    }

    override fun onPluginDataCleared() {
    }

    companion object {
        const val ID = "shared-preferences"
    }
}
