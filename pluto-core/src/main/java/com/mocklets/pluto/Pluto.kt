package com.mocklets.pluto

import android.app.Application
import androidx.lifecycle.LiveData
import com.mocklets.pluto.applifecycle.AppLifecycle
import com.mocklets.pluto.applifecycle.AppState
import com.mocklets.pluto.notch.Notch
import com.mocklets.pluto.plugin.Plugin
import com.mocklets.pluto.plugin.PluginManager
import com.mocklets.pluto.plugin.PluginSelector
import com.mocklets.pluto.plugin.PlutoStateUpdater
import com.mocklets.pluto.settings.SettingsPreferences
import com.mocklets.pluto.ui.PlutoBaseActivity
import com.mocklets.pluto.utilities.SingleLiveEvent
import java.util.LinkedHashSet

object Pluto {

    private lateinit var appLifecycle: AppLifecycle
    private var notch: Notch? = null
    internal val appState: LiveData<AppState>
        get() = appLifecycle.state

    internal val currentPlugin = SingleLiveEvent<Plugin>()
    private val stateUpdater = object : PlutoStateUpdater {
        override fun onCloseRequested() {
            close()
        }
    }
    internal val pluginManager = PluginManager(stateUpdater)

    private fun init(application: Application, plugins: LinkedHashSet<Plugin>) {
        appLifecycle = AppLifecycle()
        application.registerActivityLifecycleCallbacks(appLifecycle)
        pluginManager.install(application, plugins)
        SettingsPreferences.init(application.applicationContext)
        notch = Notch(application)
    }

    fun open() {
        appLifecycle.currentActivity?.get()?.let {
            PluginSelector().show(it, notch)
        }
    }

    private fun close() {
        appLifecycle.currentActivity?.get()?.let {
            if (it is PlutoBaseActivity) {
                it.finish()
            }
        }
    }

    fun showNotch() {
        notch?.enable(true)
    }

    fun hideNotch() {
        notch?.enable(false)
    }

    class Installer(private val application: Application) {

        private val plugins = linkedSetOf<Plugin>()

        fun addPlugin(plugin: Plugin): Installer {
            plugins.add(plugin)
            return this
        }

        fun install() {
            init(application, plugins)
        }
    }
}
