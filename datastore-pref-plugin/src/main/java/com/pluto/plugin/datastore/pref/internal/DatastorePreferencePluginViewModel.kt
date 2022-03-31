package com.pluto.plugin.datastore.pref.internal

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
import com.pluto.plugin.datastore.pref.PlutoDataStoreWatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class DatastorePreferencePluginViewModel : ViewModel() {

    internal val output: Flow<List<PrefUiModel>>
    private val expandedMap = mutableMapOf<String, MutableState<Boolean>>()

    init {
        output = PlutoDataStoreWatcher.sources.map { list ->
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

data class PrefUiModel(
    val name: String,
    val data: List<PrefElement>,
    val isExpanded: MutableState<Boolean> = mutableStateOf(true)
)

data class PrefElement(
    val prefName: String,
    val key: String,
    val value: String,
    val type: Type
)

sealed class Type(val displayText: String) {

    object TypeString : Type("string")
    object TypeBoolean : Type("boolean")
    object TypeDouble : Type("double")
    object TypeFloat : Type("float")
    object TypeLong : Type("long")
    object TypeUnknown : Type("unknown")

    companion object {
        fun <K> type(obj: K) = when (obj) {
            is String -> TypeString
            is Boolean -> TypeBoolean
            is Double -> TypeDouble
            is Long -> TypeLong
            is Float -> TypeFloat
            else -> TypeUnknown
        }
    }
}
