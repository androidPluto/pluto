package com.pluto.plugins.datastore.pref.internal

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluto.plugins.datastore.pref.PlutoDataStoreWatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
internal class DatastorePreferencePluginViewModel : ViewModel() {

    internal val output = MutableStateFlow<List<PrefUiModel>>(listOf())
    internal val filteredPref = MutableStateFlow<Map<String, Boolean>>(mapOf())
    internal val showFilterView: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val expandedMap = mutableMapOf<String, MutableState<Boolean>>()

    init {
        viewModelScope.launch {
            PlutoDataStoreWatcher.sources.map { list ->
                filteredPref.value = list.associate {
                    it.name to (filteredPref.value[it.name] ?: true)
                }
                list.map { prefHolder ->
                    prefHolder.preferences.data.map { pref ->
                        pref to prefHolder.name
                    }
                }
            }.map { listFlows ->
                combine(
                    flows = listFlows,
                    transform = { listPreferences ->
                        listPreferences.map { namePrefPair ->
                            PrefUiModel(
                                name = namePrefPair.second,
                                data = namePrefPair.first.asMap().map { entry ->
                                    PrefElement(
                                        key = entry.key.toString(),
                                        value = entry.value.toString(),
                                        type = Type.type(entry.value),
                                        prefName = namePrefPair.second
                                    )
                                },
                                isExpanded = expandedMap.getOrPut(namePrefPair.second) {
                                    mutableStateOf(false)
                                }
                            )
                        }
                    }
                )
            }.flattenMerge()
                .combine(filteredPref) { prefList, filterMap ->
                    prefList.filter {
                        filterMap[it.name] ?: true
                    }
                }.collect { list ->
                    output.value = list
                }
        }
    }

    val updateValue: (PrefElement, String) -> Unit = { preferenceElement, value ->
        viewModelScope.launch {
            val preferences = PlutoDataStoreWatcher.sources.value.find {
                it.name == preferenceElement.prefName
            }?.preferences
            preferences?.edit { preference ->
                when {
                    preferenceElement.type == Type.TypeBoolean && value.toBooleanStrictOrNull() != null ->
                        preference[booleanPreferencesKey(preferenceElement.key)] = value.toBoolean()
                    preferenceElement.type == Type.TypeDouble && value.toDoubleOrNull() != null ->
                        preference[doublePreferencesKey(preferenceElement.key)] = value.toDouble()
                    preferenceElement.type == Type.TypeFloat && value.toFloatOrNull() != null ->
                        preference[floatPreferencesKey(preferenceElement.key)] = value.toFloat()
                    preferenceElement.type == Type.TypeLong && value.toLongOrNull() != null ->
                        preference[longPreferencesKey(preferenceElement.key)] = value.toLong()
                    preferenceElement.type == Type.TypeString ->
                        preference[stringPreferencesKey(preferenceElement.key)] = value
                    else -> {
                        // show some error
                        // add validation before sending data here
                    }
                }
            }
        }
    }
}
