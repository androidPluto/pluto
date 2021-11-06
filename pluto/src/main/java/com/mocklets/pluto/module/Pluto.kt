package com.mocklets.pluto.module

import android.content.Context
import com.mocklets.pluto.plugin.Plugin

class Pluto private constructor(context: Context, plugins: LinkedHashSet<Plugin>) {

    private val pluginManager = PluginManager(plugins)

    init {
        pluginManager.install(context)
    }

    class Installer(private val context: Context) {

        private val plugins = linkedSetOf<Plugin>()

        fun addPlugin(plugin: Plugin): Installer {
            plugins.add(plugin)
            return this
        }

        fun install(): Pluto {
            return Pluto(context, plugins)
        }
    }
}
