package com.pluto.plugins.datastore.pref

import androidx.fragment.app.Fragment
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
import com.pluto.plugins.datastore.pref.internal.DatastorePreferencePluginFragment

class DataStorePrefPlugin : Plugin("Bisht") {

    override fun getConfig() = PluginConfiguration(
        name = "DataStore - pref",
        version = BuildConfig.VERSION_NAME,
        icon = R.drawable.ic_baseline_datastore_24
    )

    override fun getView(): Fragment {
        return DatastorePreferencePluginFragment()
    }

    override fun onPluginInstalled() {}

    override fun onPluginDataCleared() {}
}
