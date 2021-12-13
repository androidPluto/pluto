package com.mocklets.pluto.plugin

import android.content.Context
import com.mocklets.pluto.utilities.DebugLog

internal class PluginManager {

    private var plugins: LinkedHashSet<Plugin> = linkedSetOf()
    internal val installedPlugins: List<Plugin>
        get() {
            val list = arrayListOf<Plugin>()
            list.addAll(plugins)
            return list
        }

    fun install(context: Context, plugins: LinkedHashSet<Plugin>) {
        plugins.forEach {
            if (it.shouldInstallPlugin()) {
                it.install(context)
                this.plugins.add(it)
            } else {
                DebugLog.e("pluto_plugin", "${it.getConfig().name} not installed (reason: condition mismatch).")
            }
        }
    }
}
