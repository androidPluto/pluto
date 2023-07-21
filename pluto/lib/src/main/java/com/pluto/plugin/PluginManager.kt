package com.pluto.plugin

import android.app.Application
import com.pluto.plugin.libinterface.PlutoInterface
import com.pluto.ui.container.PlutoActivity
import com.pluto.ui.selector.SelectorActivity

internal class PluginManager(private val application: Application) {

    private var plugins: LinkedHashSet<PluginEntity> = linkedSetOf()
    internal val installedPlugins: List<PluginEntity>
        get() {
            val list = arrayListOf<PluginEntity>()
            list.addAll(plugins)
            return list
        }

    init {
        PlutoInterface.create(
            application = application, pluginActivityClass = PlutoActivity::class.java, selectorActivityClass = SelectorActivity::class.java
        )
    }

    fun install(plugins: LinkedHashSet<PluginEntity>) {
        plugins.forEach {
            when (it) {
                is Plugin -> {
                    it.install(application)
                    this.plugins.add(it)
                }

                is PluginGroup -> {
                    it.getPlugins().forEach { plugin ->
                        plugin.install(application)
                    }
                    this.plugins.add(it)
                }
            }
        }
    }

    fun get(identifier: String): Plugin? {
        plugins.forEach {
            when (it) {
                is Plugin -> if (it.identifier == identifier) return it
                is PluginGroup -> return it.getPlugins().firstOrNull { plugin -> plugin.identifier == identifier }
            }
        }
        return null
    }

    fun clearLogs(identifier: String? = null) {
        identifier?.let { get(identifier)?.onPluginDataCleared() } ?: run { clearAllLogs() }
    }

    private fun clearAllLogs() {
        installedPlugins.forEach {
            when (it) {
                is Plugin -> it.onPluginDataCleared()
                is PluginGroup -> it.getPlugins().forEach { plugin -> plugin.onPluginDataCleared() }
            }
        }
    }
}
