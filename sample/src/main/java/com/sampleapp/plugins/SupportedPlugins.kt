package com.sampleapp.plugins

import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.sampleapp.functions.network.DemoNetworkFragment

class SupportedPlugins private constructor() {
    companion object {
        internal fun get(): List<FunctionsModel> {
            return listOf(
                FunctionsModel(NETWORK, "Network & API calls"),
                FunctionsModel(EXCEPTIONS, "Exceptions & Crashes"),
                FunctionsModel(LOGGER, "Logger"),
                FunctionsModel(SHARED_PREF, "Shared Preferences"),
                FunctionsModel(ROOMS_DATABASE, "Rooms Database"),
                FunctionsModel(DATASTORE_PREF, "Datastore Preferences")
            )
        }

//        fun openPlugin(context: Context, plugin: PluginListItem) {
//            when (plugin.id) {
//                EXCEPTIONS -> Intent(context, ExceptionActivity::class.java)
//                LOGGER -> Intent(context, LoggerActivity::class.java)
//                NETWORK -> Intent(context, NetworkActivity::class.java)
//                SHARED_PREF -> Intent(context, PreferencesActivity::class.java)
//                DATASTORE_PREF -> Intent(context, DatastoreActivity::class.java)
//                ROOMS_DATABASE -> Intent(context, RoomsDBActivity::class.java)
//                else -> {
//                    Toast.makeText(context, "unsupported plugin", LENGTH_SHORT).show()
//                    null
//                }
//            }?.let {
//                context.startActivity(it)
//            }
//        }

        fun getDemoFragment(id: String): Fragment {
            return when (id) {
                NETWORK -> DemoNetworkFragment()
                else -> DemoNetworkFragment()
            }
        }

        const val DEMO: String = "demo"
        const val NETWORK: String = "network"
        const val EXCEPTIONS: String = "exceptions"
        const val SHARED_PREF: String = "shared-preferences"
        const val LOGGER: String = "logger"
        const val ROOMS_DATABASE: String = "rooms-database"
        const val DATASTORE_PREF: String = "datastore-preferences"
    }
}

@Keep
internal data class FunctionsModel(val id: String, val label: String)
