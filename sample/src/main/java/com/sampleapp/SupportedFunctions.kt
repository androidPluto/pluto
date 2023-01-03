package com.sampleapp

import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.sampleapp.functions.datastore.DemoDatastorePrefFragment
import com.sampleapp.functions.exceptions.DemoExceptionFragment
import com.sampleapp.functions.logger.DemoLoggerFragment
import com.sampleapp.functions.network.DemoNetworkFragment
import com.sampleapp.functions.roomsdatabase.DemoRoomsDatabaseFragment
import com.sampleapp.functions.sharedpreferences.DemoSharedPrefFragment

class SupportedPlugins private constructor() {
    companion object {
        internal fun get(): ArrayList<FunctionsModel> {
            return arrayListOf(
                FunctionsModel(NETWORK, "Network & API calls"),
                FunctionsModel(EXCEPTIONS, "Crashes & ANRs"),
                FunctionsModel(LOGGER, "Logger"),
                FunctionsModel(SHARED_PREF, "Shared Preferences"),
                FunctionsModel(ROOMS_DATABASE, "Rooms Database"),
                FunctionsModel(DATASTORE_PREF, "Datastore Preferences"),
                FunctionsModel(LAYOUT_INSPECTOR, "Layout Inspector")
            )
        }

        fun getDemoFragment(id: String): Fragment {
            return when (id) {
                NETWORK -> DemoNetworkFragment()
                EXCEPTIONS -> DemoExceptionFragment()
                SHARED_PREF -> DemoSharedPrefFragment()
                DATASTORE_PREF -> DemoDatastorePrefFragment()
                LOGGER -> DemoLoggerFragment()
                ROOMS_DATABASE -> DemoRoomsDatabaseFragment()
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
        const val LAYOUT_INSPECTOR: String = "layout-inspector"
        const val RULER: String = "ruler"
    }
}

@Keep
internal data class FunctionsModel(val id: String, val label: String)
