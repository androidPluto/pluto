package com.pluto.plugin.datastore.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object PlutoDataStoreWatcher {

    internal val sources = MutableStateFlow<Set<PreferenceHolder>>(emptySet())

    fun watch(name: String, store: DataStore<Preferences>) {
        sources.update { oldSet ->
            mutableSetOf<PreferenceHolder>().apply {
                addAll(oldSet)
                add(PreferenceHolder(name, store))
            }
        }
    }

    fun remove(name: String) {
        sources.update { oldSet ->
            mutableSetOf<PreferenceHolder>().apply {
                oldSet.forEach {
                    if (it.name != name) add(it)
                }
            }
        }
    }
}

internal data class PreferenceHolder(val name: String, val preferences: DataStore<Preferences>)
