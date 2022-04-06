package com.sampleapp

import android.app.Application
import com.demo.plugin.DemoPlugin
import com.pluto.Pluto
import com.pluto.plugins.datastore.pref.DataStorePrefPlugin

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Pluto.Installer(this)
            .addPlugin(DemoPlugin(DEMO_PLUGIN_ID))
            .addPlugin(DataStorePrefPlugin())
//            .addPlugin(PlutoLoggerPlugin("logger"))
//            .addPlugin(PlutoSharePreferencesPlugin("sharedPref"))
            .install()
        Pluto.showNotch(true)
    }

    companion object {
        const val DEMO_PLUGIN_ID = "demo"
    }
}
