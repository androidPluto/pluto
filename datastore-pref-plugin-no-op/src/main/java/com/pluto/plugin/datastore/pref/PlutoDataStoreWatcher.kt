package com.pluto.plugin.datastore.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object PlutoDataStoreWatcher {
    fun watch(name: String, store: DataStore<Preferences>) { } // noop
    fun remove(name: String) { } // noop
}

