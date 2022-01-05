package com.pluto.preferences

import androidx.fragment.app.Fragment
import com.pluto.plugin.Developer
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
import com.pluto.plugin.PluginOption
import com.pluto.preferences.ui.SharedPrefFragment

class PlutoSharePreferencesPlugin : Plugin() {

    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = "Shared Preferences",
        icon = R.drawable.pluto_pref___ic_pref_icon
    )

    override fun getView(): Fragment = SharedPrefFragment()

    override fun getOptions(): List<PluginOption> {
        return emptyList()
    }

    override fun getDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            website = "https://plutolib.com",
            vcsLink = "https://github.com/mocklets/pluto",
            developer = Developer(
                github = "srtvprateek",
                twitter = "srtv_prateek"
            )
        )
    }

    override fun onPluginInstalled() {
        SharedPrefRepo.init(context)
    }

    override fun onPluginDataCleared() {
    }
}
