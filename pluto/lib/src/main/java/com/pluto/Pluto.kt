package com.pluto

import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LiveData
import com.pluto.core.Session
import com.pluto.core.applifecycle.AppLifecycle
import com.pluto.core.applifecycle.AppState
import com.pluto.core.applifecycle.UiState
import com.pluto.core.notch.Notch
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginHelper.Companion.BUNDLE_LABEL
import com.pluto.plugin.PluginHelper.Companion.ID_LABEL
import com.pluto.plugin.PluginManager
import com.pluto.plugin.utilities.SingleLiveEvent
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.settings.SettingsPreferences
import com.pluto.ui.container.PlutoActivity
import com.pluto.ui.selector.SelectorActivity

object Pluto {

    private lateinit var appLifecycle: AppLifecycle
    internal var notch: Notch? = null
    internal val appState: LiveData<AppState>
        get() = appLifecycle.state

    internal val uiState: SingleLiveEvent<UiState> = SingleLiveEvent()

    internal val pluginManager = PluginManager()
    private lateinit var application: Application
    internal val session = Session()

    private fun init(application: Application, plugins: LinkedHashSet<Plugin>) {
        this.application = application
        appLifecycle = AppLifecycle()
        application.registerActivityLifecycleCallbacks(appLifecycle)
        pluginManager.install(application, plugins)
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

    internal fun close() {
        uiState.postValue(UiState.Close)
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
