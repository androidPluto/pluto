package com.pluto.preferences

import androidx.fragment.app.Fragment
import com.pluto.plugin.Developer
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration

class PlutoSharePreferencesPlugin : Plugin() {

    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = context.getString(R.string.pluto_pref___plugin_name),
        icon = R.drawable.pluto_pref___ic_pref_icon
    )

    override fun getView(): Fragment = SharedPrefFragment()

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
