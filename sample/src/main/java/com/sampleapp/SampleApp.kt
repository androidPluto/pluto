package com.sampleapp

import android.app.Application
import com.pluto.Pluto
import com.pluto.logger.PlutoLoggerPlugin
import com.pluto.preferences.PlutoSharePreferencesPlugin

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Pluto.Installer(this)
            .addPlugin(PlutoLoggerPlugin("logger"))
            .addPlugin(PlutoSharePreferencesPlugin("sharedPref"))
            .install()
        Pluto.showNotch(false)
    }
}
