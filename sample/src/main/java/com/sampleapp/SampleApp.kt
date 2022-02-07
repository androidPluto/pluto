package com.sampleapp

import android.app.Application
import com.pluto.Pluto
import com.pluto.logger.PlutoLoggerPlugin
import com.pluto.logger.PlutoTimberTree
import timber.log.Timber

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Pluto.Installer(this)
            .addPlugin(PlutoLoggerPlugin("logger"))
//            .addPlugin(PlutoSharePreferencesPlugin("sharedPref"))
            .install()
        Pluto.showNotch(true)

        Timber.plant(PlutoTimberTree())
    }
}
