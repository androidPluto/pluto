package com.pluto.plugin.datastore.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

@Suppress("UnusedPrivateMember", "EmptyFunctionBlock")
object PlutoDataStoreWatcher {
    fun watch(name: String, store: DataStore<Preferences>) { } // noop
    fun remove(name: String) { } // noop
}
