package com.pluto.plugins.datastore.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

@Suppress("UnusedPrivateMember", "EmptyFunctionBlock")
object PlutoDatastoreWatcher {
    fun watch(name: String, store: DataStore<Preferences>) { } // noop
    fun remove(name: String) { } // noop
}
