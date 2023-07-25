package com.sampleapp

import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.pluto.plugins.datastore.pref.PlutoDatastorePreferencesPlugin
import com.pluto.plugins.exceptions.PlutoExceptionsPlugin
import com.pluto.plugins.layoutinspector.PlutoLayoutInspectorPlugin
import com.pluto.plugins.logger.PlutoLoggerPlugin
import com.pluto.plugins.network.PlutoNetworkPlugin
import com.pluto.plugins.preferences.PlutoSharePreferencesPlugin
import com.pluto.plugins.rooms.db.PlutoRoomsDatabasePlugin
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
                FunctionsModel(PlutoNetworkPlugin.ID, "Network & API calls"),
                FunctionsModel(PlutoExceptionsPlugin.ID, "Crashes & ANRs"),
                FunctionsModel(PlutoLoggerPlugin.ID, "Logger"),
                FunctionsModel(PlutoSharePreferencesPlugin.ID, "Shared Preferences"),
                FunctionsModel(PlutoRoomsDatabasePlugin.ID, "Rooms Database"),
                FunctionsModel(PlutoDatastorePreferencesPlugin.ID, "Datastore Preferences"),
                FunctionsModel(PlutoLayoutInspectorPlugin.ID, "Layout Inspector")
            )
        }

        fun getDemoFragment(id: String): Fragment {
            return when (id) {
                PlutoNetworkPlugin.ID -> DemoNetworkFragment()
                PlutoExceptionsPlugin.ID -> DemoExceptionFragment()
                PlutoSharePreferencesPlugin.ID -> DemoSharedPrefFragment()
                PlutoDatastorePreferencesPlugin.ID -> DemoDatastorePrefFragment()
                PlutoLoggerPlugin.ID -> DemoLoggerFragment()
                PlutoRoomsDatabasePlugin.ID -> DemoRoomsDatabaseFragment()
                else -> DemoNetworkFragment()
            }
        }
    }
}

@Keep
internal data class FunctionsModel(val id: String, val label: String)
