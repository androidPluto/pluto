package com.mocklets.pluto

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import com.mocklets.pluto.applifecycle.AppLifecycle
import com.mocklets.pluto.applifecycle.AppState
import com.mocklets.pluto.notch.Notch
import com.mocklets.pluto.plugin.Plugin
import com.mocklets.pluto.plugin.PluginManager
import com.mocklets.pluto.settings.SettingsPreferences
import com.mocklets.pluto.ui.PlutoBaseActivity
import java.util.LinkedHashSet

object Pluto {

    internal val pluginManager = PluginManager()
    private lateinit var appLifecycle: AppLifecycle
    private var notch: Notch? = null
    internal val appState: LiveData<AppState>
        get() = appLifecycle.state

    private fun init(application: Application, plugins: LinkedHashSet<Plugin>) {
        appLifecycle = AppLifecycle()
        application.registerActivityLifecycleCallbacks(appLifecycle)
        pluginManager.install(application.applicationContext, plugins)
        SettingsPreferences.init(application.applicationContext)
        notch = Notch(application)
    }

    fun open(context: Context) {
        val intent = Intent(context, PlutoBaseActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
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
