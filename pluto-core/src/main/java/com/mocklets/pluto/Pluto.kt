package com.mocklets.pluto

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.mocklets.pluto.plugin.Plugin
import com.mocklets.pluto.plugin.PluginManager
import com.mocklets.pluto.ui.PlutoActivity
import java.util.LinkedHashSet

object Pluto {

    @SuppressLint("StaticFieldLeak")
    internal var pluginManager = PluginManager()

    private fun init(context: Context, plugins: LinkedHashSet<Plugin>) {
        pluginManager.install(context, plugins)
    }

    fun open(context: Context) {
        val intent = Intent(context, PlutoActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    class Installer(private val context: Context) {

        private val plugins = linkedSetOf<Plugin>()

        fun addPlugin(plugin: Plugin): Installer {
            plugins.add(plugin)
            return this
        }

        fun install() {
            init(context.applicationContext, plugins)
        }
    }
}
