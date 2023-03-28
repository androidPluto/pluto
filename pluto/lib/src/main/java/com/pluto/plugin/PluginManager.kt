package com.pluto.plugin

import android.app.Application
import com.pluto.plugin.libinterface.PlutoInterface
import com.pluto.ui.container.PlutoActivity
import com.pluto.ui.selector.SelectorActivity
import com.pluto.utilities.DebugLog

internal class PluginManager(private val application: Application) {

    private var plugins: LinkedHashSet<Plugin> = linkedSetOf()
    internal val installedPlugins: List<Plugin>
        get() {
            val list = arrayListOf<Plugin>()
            list.addAll(plugins)
            return list
        }

    init {
        PlutoInterface.create(
            application = application,
            pluginActivityClass = PlutoActivity::class.java,
            selectorActivityClass = SelectorActivity::class.java
        )
    }

    fun install(plugins: LinkedHashSet<Plugin>) {
        plugins.forEach {
            if (it.shouldInstallPlugin()) {
                it.install(application)
                this.plugins.add(it)
            } else {
                DebugLog.e("pluto_plugin", "${it.getConfig().name} not installed (reason: condition mismatch).")
            }
        }
    }

    fun get(identifier: String): Plugin? {
        return plugins.firstOrNull {
            it.devIdentifier == identifier
        }
    }
}
