package com.sampleapp

import android.app.Application
import android.util.Log
import com.demo.plugin.DemoPlugin
import com.pluto.Pluto
import com.pluto.plugins.exceptions.PlutoExceptions
import com.pluto.plugins.exceptions.PlutoExceptionsPlugin
import com.pluto.plugins.logger.PlutoLoggerPlugin
import com.pluto.plugins.logger.PlutoTimberTree
import com.pluto.plugins.network.PlutoNetworkPlugin
import com.pluto.plugins.preferences.PlutoSharePreferencesPlugin
import kotlin.system.exitProcess
import timber.log.Timber

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Pluto.Installer(this)
            .addPlugin(DemoPlugin(DEMO_PLUGIN_ID))
            .addPlugin(PlutoExceptionsPlugin(EXCEPTIONS_PLUGIN_ID))
            .addPlugin(PlutoNetworkPlugin(NETWORK_PLUGIN_ID))
            .addPlugin(PlutoLoggerPlugin(LOGGER_PLUGIN_ID))
            .addPlugin(PlutoSharePreferencesPlugin(PREF_PLUGIN_ID))
            .install()
        Pluto.showNotch(true)

        plantPlutoTimber()
        setExceptionListener()
    }

    /**
     * Logger Timber handler
     */
    private fun plantPlutoTimber() {
        Timber.plant(PlutoTimberTree())
    }

    /**
     * Exception handler
     */
    private fun setExceptionListener() {
        PlutoExceptions.setExceptionHandler { thread, throwable ->
            Log.d("exception_demo", "uncaught exception handled on thread: " + thread.name, throwable)
            exitProcess(0)
        }
    }

    companion object {
        const val DEMO_PLUGIN_ID = "demo"
        const val EXCEPTIONS_PLUGIN_ID = "exceptions"
        const val NETWORK_PLUGIN_ID = "network"
        const val LOGGER_PLUGIN_ID = "logger"
        const val PREF_PLUGIN_ID = "sharedPref"
    }
}
