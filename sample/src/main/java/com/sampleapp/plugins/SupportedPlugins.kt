package com.sampleapp.plugins

import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.sampleapp.list.PluginListItem
import com.sampleapp.plugins.datastore.DatastoreActivity
import com.sampleapp.plugins.exceptions.ExceptionActivity
import com.sampleapp.plugins.logger.LoggerActivity
import com.sampleapp.plugins.network.NetworkActivity
import com.sampleapp.plugins.preferences.PreferencesActivity
import com.sampleapp.plugins.roomsDatabase.RoomsDBActivity

class SupportedPlugins private constructor() {
    companion object {
        fun get(): List<PluginListItem> {
            return listOf(
                PluginListItem(NETWORK),
                PluginListItem(EXCEPTIONS),
                PluginListItem(LOGGER),
                PluginListItem(SHARED_PREF),
                PluginListItem(DATABASE),
                PluginListItem(DATASTORE_PREF)
            )
        }

        fun openPlugin(context: Context, plugin: PluginListItem) {
            when (plugin.title) {
                EXCEPTIONS -> Intent(context, ExceptionActivity::class.java)
                LOGGER -> Intent(context, LoggerActivity::class.java)
                NETWORK -> Intent(context, NetworkActivity::class.java)
                SHARED_PREF -> Intent(context, PreferencesActivity::class.java)
                DATASTORE_PREF -> Intent(context, DatastoreActivity::class.java)
                DATABASE -> Intent(context, RoomsDBActivity::class.java)
                else -> {
                    Toast.makeText(context, "unsupported plugin", LENGTH_SHORT).show()
                    null
                }
            }?.let {
                context.startActivity(it)
            }
        }

        const val DEMO: String = "demo"
        const val NETWORK: String = "network & API calls"
        const val EXCEPTIONS: String = "exceptions & crashes"
        const val SHARED_PREF: String = "shared preferences"
        const val LOGGER: String = "logger"
        const val DATABASE: String = "rooms database"
        const val DATASTORE_PREF: String = "datastore preferences"
    }
}
