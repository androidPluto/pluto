package com.mocklets.pluto.plugin

import android.content.Context

internal class PluginManager {

    private var plugins: LinkedHashSet<Plugin> = linkedSetOf()
    private var context: Context? = null
    internal val installedPlugins: List<Plugin>
        get() {
            val list = arrayListOf<Plugin>()
            context?.let {
                list.addAll(plugins)
            }
            return list
        }

    fun install(context: Context, plugins: LinkedHashSet<Plugin>) {
        this.context = context.applicationContext
        this.plugins = plugins
        plugins.forEach {
            if (it.shouldInstallPlugin()) {
                it.install(context)
            }
        }
    }
}
