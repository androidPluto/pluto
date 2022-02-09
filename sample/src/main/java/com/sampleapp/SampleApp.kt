package com.sampleapp

import android.app.Application
import com.demo.plugin.DemoPlugin
import com.pluto.Pluto

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Pluto.Installer(this)
            .addPlugin(DemoPlugin("demo"))
//            .addPlugin(PlutoLoggerPlugin("logger"))
//            .addPlugin(PlutoSharePreferencesPlugin("sharedPref"))
            .install()
        Pluto.showNotch(true)
    }
}
