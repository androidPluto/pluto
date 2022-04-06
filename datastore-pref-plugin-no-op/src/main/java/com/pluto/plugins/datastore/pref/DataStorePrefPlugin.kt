package com.pluto.plugins.datastore.pref

import androidx.fragment.app.Fragment
import com.pluto.plugins.Plugin
import com.pluto.plugins.PluginConfiguration
import java.lang.IllegalStateException

class DataStorePrefPlugin : Plugin("Bisht") {

    override fun getConfig() = PluginConfiguration(
        name = "DataStore - preference",
        version = "0.1-beta"
    )

    override fun getView(): Fragment {
        throw IllegalStateException("noop implementation. this should not be called")
    }

    override fun onPluginInstalled() {}

    override fun onPluginDataCleared() {}
}
