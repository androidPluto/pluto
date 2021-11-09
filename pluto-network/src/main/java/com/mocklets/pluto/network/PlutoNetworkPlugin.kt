package com.mocklets.pluto.network

import android.util.Log
import com.mocklets.pluto.plugin.Plugin
import com.mocklets.pluto.plugin.PluginConfiguration

class PlutoNetworkPlugin : Plugin() {

//    init {
//        PlutoNetwork.appContext = context.applicationContext
//    }

    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = "Network Calls"
    )

    override fun onPluginInstalled() {
        Log.e("prateek", "network installed ${getConfig().identifier}")
        PlutoNetwork.appContext = context.applicationContext
    }
}
