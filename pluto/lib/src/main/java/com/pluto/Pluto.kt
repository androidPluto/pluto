package com.pluto

import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.pluto.core.Session
import com.pluto.core.applifecycle.AppLifecycle
import com.pluto.core.callback.ResetDataCallback
import com.pluto.core.notch.Notch
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginHelper.Companion.BUNDLE_LABEL
import com.pluto.plugin.PluginHelper.Companion.ID_LABEL
import com.pluto.plugin.PluginManager
import com.pluto.settings.SettingsPreferences
import com.pluto.tools.ToolManager
import com.pluto.ui.container.PlutoActivity
import com.pluto.ui.selector.SelectorActivity
import com.pluto.utilities.extensions.toast

object Pluto {

    internal lateinit var appLifecycle: AppLifecycle
    private var notch: Notch? = null

    internal val pluginManager = PluginManager()
    internal val toolManager = ToolManager()
    private lateinit var application: Application
    internal val session = Session()

    internal lateinit var resetDataCallback: ResetDataCallback

    private fun init(application: Application, plugins: LinkedHashSet<Plugin>) {
        resetDataCallback = ResetDataCallback()
        this.application = application
        appLifecycle = AppLifecycle()
        application.registerActivityLifecycleCallbacks(appLifecycle)
        pluginManager.install(application, plugins)
        toolManager.initialise(application)
        SettingsPreferences.init(application.applicationContext)
        notch = Notch(application, appLifecycle.shouldShowNotch)
    }

    @JvmOverloads
    fun open(identifier: String? = null, bundle: Bundle? = null) {
        val intent: Intent?
        if (identifier != null) {
            pluginManager.get(identifier)?.let {
                intent = Intent(application.applicationContext, PlutoActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra(ID_LABEL, identifier)
                intent.putExtra(BUNDLE_LABEL, bundle)
                application.applicationContext.startActivity(intent)
                return
            }
            application.applicationContext.toast("Plugin [$identifier] not installed")
        } else {
            intent = Intent(application.applicationContext, SelectorActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            application.applicationContext.startActivity(intent)
        }
    }

    fun showNotch(state: Boolean) {
        notch?.enable(state)
    }

    fun clearAllLogs() {
        pluginManager.installedPlugins.forEach { it.onPluginDataCleared() }
    }

    fun clearLogs(plugin: Plugin) {
        if (pluginManager.installedPlugins.contains(plugin)) {
            plugin.onPluginDataCleared()
        }
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
