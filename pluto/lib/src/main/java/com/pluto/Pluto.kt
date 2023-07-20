package com.pluto

import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.pluto.core.Session
import com.pluto.core.applifecycle.AppLifecycle
import com.pluto.core.applifecycle.AppStateCallback
import com.pluto.core.notch.Notch
import com.pluto.core.notch.NotchStateCallback
import com.pluto.core.notification.NotificationManager
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginEntity
import com.pluto.plugin.PluginGroup
import com.pluto.plugin.PluginManager
import com.pluto.plugin.libinterface.NotificationInterface.Companion.BUNDLE_LABEL
import com.pluto.plugin.libinterface.NotificationInterface.Companion.ID_LABEL
import com.pluto.settings.ResetDataCallback
import com.pluto.tool.ToolManager
import com.pluto.ui.container.PlutoActivity
import com.pluto.ui.selector.SelectorActivity
import com.pluto.ui.selector.SelectorStateCallback
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.settings.SettingsPreferences

object Pluto {

    private lateinit var appLifecycle: AppLifecycle
    private lateinit var application: Application
    private var notch: Notch? = null

    internal lateinit var pluginManager: PluginManager
    internal lateinit var toolManager: ToolManager
    private lateinit var notificationManager: NotificationManager

    internal val session = Session()

    internal lateinit var resetDataCallback: ResetDataCallback
    internal lateinit var appStateCallback: AppStateCallback
    internal lateinit var selectorStateCallback: SelectorStateCallback
    private lateinit var notchStateCallback: NotchStateCallback

    private fun init(application: Application, plugins: LinkedHashSet<PluginEntity>) {
        initialiseCallbacks()
        this.application = application
        appLifecycle = AppLifecycle(appStateCallback)
        application.registerActivityLifecycleCallbacks(appLifecycle)
        pluginManager = PluginManager(application).apply {
            install(plugins)
        }
        toolManager = ToolManager(application, appStateCallback.state).apply {
            initialise()
        }
        notificationManager = NotificationManager(application, appStateCallback.state)
        SettingsPreferences.init(application.applicationContext)
        notch = Notch(application, notchStateCallback.state)
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

    private fun initialiseCallbacks() {
        resetDataCallback = ResetDataCallback()
        appStateCallback = AppStateCallback()
        selectorStateCallback = SelectorStateCallback()
        notchStateCallback = NotchStateCallback(appStateCallback.state, selectorStateCallback.state)
    }

    class Installer(private val application: Application) {

        private val plugins = linkedSetOf<PluginEntity>()

        fun addPlugin(plugin: Plugin): Installer {
            plugins.add(plugin)
            return this
        }

        fun addPluginGroup(pluginGroup: PluginGroup): Installer {
            plugins.add(pluginGroup)
            return this
        }

        fun install() {
            init(application, plugins)
        }
    }
}
