package com.sampleapp

import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.sampleapp.list.PluginListItem
import com.sampleapp.plugins.exceptions.ExceptionActivity
import com.sampleapp.plugins.logger.LoggerActivity
import com.sampleapp.plugins.network.NetworkActivity
import com.sampleapp.plugins.preferences.PreferencesActivity

class SupportedPlugins private constructor() {
    companion object {
        fun get(): List<PluginListItem> {
            return listOf(
                PluginListItem(NETWORK),
                PluginListItem(EXCEPTIONS),
                PluginListItem(PREFERENCES),
                PluginListItem(LOGGER)
            )
        }

        fun openPlugin(context: Context, plugin: PluginListItem) {
            when (plugin.title) {
                EXCEPTIONS -> Intent(context, ExceptionActivity::class.java)
                LOGGER -> Intent(context, LoggerActivity::class.java)
                NETWORK -> Intent(context, NetworkActivity::class.java)
                PREFERENCES -> Intent(context, PreferencesActivity::class.java)
                else -> {
                    Toast.makeText(context, "unsupported plugin", LENGTH_SHORT).show()
                    null
                }
            }?.let {
                context.startActivity(it)
            }
        }

        private const val NETWORK: String = "network"
        private const val EXCEPTIONS: String = "exceptions"
        private const val PREFERENCES: String = "preferences"
        private const val LOGGER: String = "LOGGER"
    }
}
