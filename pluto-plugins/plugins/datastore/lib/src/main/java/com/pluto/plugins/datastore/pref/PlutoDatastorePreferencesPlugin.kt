package com.pluto.plugins.datastore.pref

import androidx.fragment.app.Fragment
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
import com.pluto.plugins.datastore.pref.internal.BaseFragment

class PlutoDatastorePreferencesPlugin() : Plugin(ID) {

    @SuppressWarnings("UnusedPrivateMember")
    @Deprecated("Use the default constructor PlutoDatastorePreferencesPlugin() instead.")
    constructor(identifier: String) : this()

    override fun getConfig() = PluginConfiguration(
        name = "DataStore Preferences",
        version = BuildConfig.VERSION_NAME,
        icon = R.drawable.pluto_dts___ic_logo
    )

    override fun getDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            website = "https://androidpluto.com",
            vcsLink = "https://github.com/androidPluto/pluto",
            twitter = "https://twitter.com/android_pluto"
        )
    }

    override fun getView(): Fragment {
        return BaseFragment()
    }

    override fun onPluginInstalled() {}

    override fun onPluginDataCleared() {}

    companion object {
        const val ID = "datastore-preferences"
    }
}
