package com.mocklets.pluto.plugin

import android.app.Application
import com.mocklets.pluto.plugin.utilities.DebugLog

internal class PluginManager {

    private var plugins: LinkedHashSet<Plugin> = linkedSetOf()
    internal val installedPlugins: List<Plugin>
        get() {
            val list = arrayListOf<Plugin>()
            list.addAll(plugins)
            return list
        }

    fun install(application: Application, plugins: LinkedHashSet<Plugin>) {
        plugins.forEach {
            if (it.shouldInstallPlugin()) {
                it.install(application)
                this.plugins.add(it)
            } else {
                DebugLog.e("pluto_plugin", "${it.getConfig().name} not installed (reason: condition mismatch).")
            }
        }
    }
}
