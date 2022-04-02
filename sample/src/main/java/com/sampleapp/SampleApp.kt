package com.sampleapp

import android.app.Application
import com.demo.plugin.DemoPlugin
import com.pluto.Pluto
import com.pluto.plugins.exceptions.PlutoExceptionsPlugin
import com.pluto.plugins.network.PlutoNetworkPlugin

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Pluto.Installer(this)
            .addPlugin(DemoPlugin(DEMO_PLUGIN_ID))
            .addPlugin(PlutoExceptionsPlugin(EXCEPTIONS_PLUGIN_ID))
            .addPlugin(PlutoNetworkPlugin(NETWORK_PLUGIN_ID))
//            .addPlugin(PlutoLoggerPlugin("logger"))
//            .addPlugin(PlutoSharePreferencesPlugin("sharedPref"))
            .install()
        Pluto.showNotch(true)
    }

    companion object {
        const val DEMO_PLUGIN_ID = "demo"
        const val EXCEPTIONS_PLUGIN_ID = "exceptions"
        const val NETWORK_PLUGIN_ID = "network"
    }
}
