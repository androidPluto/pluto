package com.pluto.plugin.datastore.pref

import androidx.fragment.app.Fragment
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
import com.pluto.plugin.datastore.pref.internal.DatastorePreferencePluginFragment

class DataStorePrefPlugin : Plugin("Bisht") {

    override fun getConfig() = PluginConfiguration(
        name = "DataStore - preference",
        version = "0.1-beta"
    )

    override fun getView(): Fragment {
        return DatastorePreferencePluginFragment()
    }

    override fun onPluginInstalled() {}

    override fun onPluginDataCleared() {}
}
