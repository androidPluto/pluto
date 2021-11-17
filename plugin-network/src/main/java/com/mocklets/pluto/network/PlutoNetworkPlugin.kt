package com.mocklets.pluto.network

import android.util.Log
import androidx.fragment.app.Fragment
import com.mocklets.pluto.network.ui.NetworkFragment
import com.mocklets.pluto.plugin.Developer
import com.mocklets.pluto.plugin.DeveloperDetails
import com.mocklets.pluto.plugin.Plugin
import com.mocklets.pluto.plugin.PluginConfiguration

class PlutoNetworkPlugin : Plugin() {

//    init {
//        PlutoNetwork.appContext = context.applicationContext
//    }

    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = "Network Calls"
    )

    override fun getDeveloperDeveloperDetails(): DeveloperDetails {
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
        Log.e("prateek", "network installed ${getConfig().identifier}")
        PlutoNetwork.appContext = context.applicationContext
    }

    override fun onPluginSelected(): Fragment {
        return NetworkFragment()
    }
}
