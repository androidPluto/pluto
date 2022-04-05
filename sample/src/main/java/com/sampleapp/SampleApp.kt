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
import com.pluto.plugins.rooms.db.PlutoRoomsDatabasePlugin
import com.sampleapp.plugins.SupportedPlugins.Companion.DATABASE
import com.sampleapp.plugins.SupportedPlugins.Companion.DEMO
import com.sampleapp.plugins.SupportedPlugins.Companion.EXCEPTIONS
import com.sampleapp.plugins.SupportedPlugins.Companion.LOGGER
import com.sampleapp.plugins.SupportedPlugins.Companion.NETWORK
import com.sampleapp.plugins.SupportedPlugins.Companion.PREFERENCES
import kotlin.system.exitProcess
import timber.log.Timber

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Pluto.Installer(this)
            .addPlugin(DemoPlugin(DEMO))
            .addPlugin(PlutoExceptionsPlugin(EXCEPTIONS))
            .addPlugin(PlutoNetworkPlugin(NETWORK))
            .addPlugin(PlutoLoggerPlugin(LOGGER))
            .addPlugin(PlutoSharePreferencesPlugin(PREFERENCES))
            .addPlugin(PlutoRoomsDatabasePlugin(DATABASE))
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
}
