package com.sampleapp.pluto

import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginGroup
import com.pluto.plugin.PluginGroupConfiguration
import com.pluto.plugins.datastore.pref.PlutoDatastorePreferencesPlugin
import com.pluto.plugins.preferences.PlutoSharePreferencesPlugin
import com.pluto.plugins.rooms.db.PlutoRoomsDatabasePlugin

class DataSourcePluginGroup : PluginGroup(ID) {

    companion object {
        const val ID = "datasource-group"
    }

    override fun getConfig(): PluginGroupConfiguration = PluginGroupConfiguration(
        name = "DataSource Group"
    )

    override fun getPlugins(): List<Plugin> = listOf(
        PlutoSharePreferencesPlugin(),
        PlutoDatastorePreferencesPlugin(),
        PlutoRoomsDatabasePlugin()
    )
}
