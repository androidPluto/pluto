package com.pluto.plugin

import android.app.Application

abstract class PluginGroup(identifier: String) : PluginEntity(identifier) {

    private var plugins: LinkedHashSet<Plugin> = linkedSetOf()

    val installedPlugins: List<Plugin>
        get() {
            val list = arrayListOf<Plugin>()
            list.addAll(plugins)
            return list
        }

    abstract fun getConfig(): PluginGroupConfiguration

    protected abstract fun getPlugins(): List<Plugin>

    final override fun install(application: Application) {
        getPlugins().forEach {
            it.install(application)
            plugins.add(it)
        }
    }
}
