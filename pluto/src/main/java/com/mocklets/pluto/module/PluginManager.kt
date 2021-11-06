package com.mocklets.pluto.module

import android.content.Context
import com.mocklets.pluto.plugin.Plugin

internal class PluginManager(private val plugins: LinkedHashSet<Plugin>) {

    fun install(context: Context) {
        plugins.forEach {
            if (it.shouldInstallPlugin()) {
                it.install(context)
            }
        }
    }
}
