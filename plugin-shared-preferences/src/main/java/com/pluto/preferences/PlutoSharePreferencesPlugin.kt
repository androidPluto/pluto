package com.pluto.preferences

import androidx.fragment.app.Fragment
import com.mocklets.pluto.plugin.Developer
import com.mocklets.pluto.plugin.DeveloperDetails
import com.mocklets.pluto.plugin.Plugin
import com.mocklets.pluto.plugin.PluginConfiguration
import com.pluto.preferences.ui.SharedPrefFragment

class PlutoSharePreferencesPlugin : Plugin() {

    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = "Shared Preferences",
        icon = R.drawable.pluto_pref___ic_pref_icon
    )

    override fun getView(): Fragment = SharedPrefFragment()

    override fun getDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            website = "https://pluto.mocklets.com",
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
}
