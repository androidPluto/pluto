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
                    preferenceElement.type == Type.TYPE_BOOLEAN && value.toBooleanStrictOrNull() != null -> {
                        preference[booleanPreferencesKey(preferenceElement.key)] = value.toBoolean()
                    }
                    preferenceElement.type == Type.TYPE_DOUBLE && value.toDoubleOrNull() != null -> {
                        preference[doublePreferencesKey(preferenceElement.key)] = value.toDouble()
                    }
                    preferenceElement.type == Type.TYPE_FLOAT && value.toFloatOrNull() != null -> {
                        preference[floatPreferencesKey(preferenceElement.key)] = value.toFloat()
                    }
                    preferenceElement.type == Type.TYPE_LONG && value.toLongOrNull() != null -> {
                        preference[longPreferencesKey(preferenceElement.key)] = value.toLong()
                    }
                    preferenceElement.type == Type.TYPE_STRING -> {
                        preference[stringPreferencesKey(preferenceElement.key)] = value
                    }
                    else -> {
                        // show some error
                        // add validation before sending data here
                    }
                }
            }
        }
    }
}

class PrefUiModel(
    val name: String,
    val data: List<PrefElement>,
    val isExpanded: MutableState<Boolean> = mutableStateOf(true)
)

class PrefElement(
    val prefName: String,
    val key: String,
    val value: String,
    val type: Type
)

sealed class Type(val displayText: String) {

    object TYPE_STRING : Type("string")
    object TYPE_BOOLEAN : Type("boolean")
    object TYPE_DOUBLE : Type("double")
    object TYPE_FLOAT : Type("float")
    object TYPE_LONG : Type("long")
    object TYPE_UNKNOWN : Type("unknown")

    companion object {
        fun <K> type(obj: K) = when (obj) {
            is String -> TYPE_STRING
            is Boolean -> TYPE_BOOLEAN
            is Double -> TYPE_DOUBLE
            is Long -> TYPE_LONG
            is Float -> TYPE_FLOAT
            else -> TYPE_UNKNOWN
        }
    }
}
