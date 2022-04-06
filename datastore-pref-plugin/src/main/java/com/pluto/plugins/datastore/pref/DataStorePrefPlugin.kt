package com.pluto.plugins.datastore.pref

import androidx.fragment.app.Fragment
import com.pluto.plugins.Plugin
import com.pluto.plugins.PluginConfiguration
import com.pluto.plugins.datastore.pref.internal.DatastorePreferencePluginFragment

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
