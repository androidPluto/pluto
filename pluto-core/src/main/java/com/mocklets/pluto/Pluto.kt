package com.mocklets.pluto

import android.app.Application
import android.content.Context
import android.content.Intent
import com.mocklets.pluto.plugin.Plugin
import com.mocklets.pluto.plugin.PluginManager
import com.mocklets.pluto.settings.SettingsPreferences
import com.mocklets.pluto.ui.PlutoBaseActivity
import java.util.LinkedHashSet

object Pluto {

    internal val pluginManager = PluginManager()
    internal lateinit var appLifecycleTracker: AppLifecycleTracker

    private fun init(context: Context, plugins: LinkedHashSet<Plugin>) {
        pluginManager.install(context, plugins)
        SettingsPreferences.init(context)
        appLifecycleTracker = AppLifecycleTracker(context.applicationContext as Application)
    }

    fun open(context: Context) {
        val intent = Intent(context, PlutoBaseActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    @JvmOverloads
    fun showControls(enableNotch: Boolean = true) {
        appLifecycleTracker.start(enableNotch)
    }

    fun hideControls() {
        appLifecycleTracker.stop()
    }

    class Installer(private val context: Context) {

        private val plugins = linkedSetOf<Plugin>()

        fun addPlugin(plugin: Plugin): Installer {
            plugins.add(plugin)
            return this
        }

        fun install() {
            init(context.applicationContext, plugins)
        }
    }
}
