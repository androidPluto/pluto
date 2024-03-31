package com.pluto.plugins.datastore.pref

import androidx.annotation.Keep
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pluto.utilities.selector.SelectorOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

object PlutoDatastoreWatcher {

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

    internal fun getSource(name: String): PreferenceHolder {
        return sources.value.toList().first { it.name == name }
    }
}

@Keep
internal data class PreferenceHolder(val name: String, val preferences: DataStore<Preferences>) : SelectorOption() {
    override fun displayText(): CharSequence = name
}
