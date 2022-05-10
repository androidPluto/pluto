package com.pluto.plugins.network

import androidx.fragment.app.Fragment
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
import com.pluto.plugins.network.internal.NetworkFragment
import com.pluto.plugins.network.internal.interceptor.logic.NetworkCallsRepo
import com.pluto.plugins.network.internal.mock.logic.MockSettingsRepo

class PlutoNetworkPlugin(devIdentifier: String) : Plugin(devIdentifier) {
    override fun getConfig(): PluginConfiguration {
        return PluginConfiguration(
            name = context.getString(R.string.pluto_network___plugin_name),
            icon = R.drawable.pluto_network___ic_plugin_logo,
            version = BuildConfig.VERSION_NAME
        )
    }

    override fun getDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            vcsLink = "https://github.com/plutolib/pluto",
            website = "https://plutolib.com",
            twitter = "https://twitter.com/pluto_lib"
        )
    }

    override fun getView(): Fragment = NetworkFragment()
    override fun onPluginDataCleared() {
        NetworkCallsRepo.deleteAll()
    }

    override fun onPluginInstalled() {
        PlutoNetwork.initialize(context)
        MockSettingsRepo.init(context)
    }
}
