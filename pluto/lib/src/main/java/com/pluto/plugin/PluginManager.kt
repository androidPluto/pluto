package com.pluto.plugin

import android.app.Application
import com.pluto.plugin.libinterface.PlutoInterface
import com.pluto.ui.container.PlutoActivity
import com.pluto.ui.selector.SelectorActivity

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

    fun install(plugins: LinkedHashSet<PluginEntity>) {
        plugins.forEach {
            when (it) {
                is Plugin -> {
                    it.install(application)
                    this.plugins.add(it)
                }
            }
        }
    }

    fun get(identifier: String): Plugin? {
        return plugins.firstOrNull {
            it.identifier == identifier
        }
    }
}
