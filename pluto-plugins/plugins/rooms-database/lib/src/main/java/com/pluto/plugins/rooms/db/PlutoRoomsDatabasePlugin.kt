package com.pluto.plugins.rooms.db

import androidx.fragment.app.Fragment
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
import com.pluto.plugins.rooms.db.internal.ui.filter.FilterConfig

class PlutoRoomsDatabasePlugin(identifier: String) : Plugin(identifier) {

    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = context.getString(R.string.pluto_rooms___plugin_name),
        icon = R.drawable.pluto_rooms___ic_rooms_icon,
        version = BuildConfig.VERSION_NAME
    )

    override fun getView(): Fragment = RoomsDBFragment()

    override fun getDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            website = "https://androidpluto.com",
            vcsLink = "https://github.com/androidPluto/pluto",
            twitter = "https://twitter.com/android_pluto"
        )
    }

    override fun onPluginInstalled() {
    }

    override fun onPluginDataCleared() {
        FilterConfig.clear()
    }
}
