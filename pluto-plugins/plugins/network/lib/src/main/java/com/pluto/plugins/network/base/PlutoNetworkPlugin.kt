package com.pluto.plugins.network.base

import androidx.fragment.app.Fragment
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
import com.pluto.plugins.network.base.internal.NetworkFragment
import com.pluto.plugins.network.base.internal.interceptor.logic.NetworkCallsRepo
import com.pluto.plugins.network.base.internal.mock.logic.MockSettingsRepo

class PlutoNetworkPlugin() : Plugin(ID) {

    @SuppressWarnings("UnusedPrivateMember")
    @Deprecated("Use the default constructor PlutoNetworkPlugin() instead.")
    constructor(identifier: String) : this()

    override fun getConfig(): PluginConfiguration {
        return PluginConfiguration(
            name = context.getString(R.string.pluto_network___plugin_name),
            icon = R.drawable.pluto_network___ic_plugin_logo,
            version = BuildConfig.VERSION_NAME
        )
    }

    override fun getDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            website = "https://androidpluto.com",
            vcsLink = "https://github.com/androidpluto/pluto",
            twitter = "https://twitter.com/android_pluto"
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

    companion object {
        const val ID = "network"
    }
}
