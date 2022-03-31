package com.pluto.plugin.datastore.pref

import androidx.fragment.app.Fragment
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
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
