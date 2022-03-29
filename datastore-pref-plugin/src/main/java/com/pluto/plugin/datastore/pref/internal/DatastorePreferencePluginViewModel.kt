package com.pluto.plugin.datastore.pref.internal

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.pluto.plugin.datastore.pref.PlutoDataStoreWatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
class DatastorePreferencePluginViewModel : ViewModel() {

    val output: Flow<List<PrefUiModel>>

    init {
        output = PlutoDataStoreWatcher.sources.map { list ->
            list.map { prefHolder ->
                prefHolder.preferences.data.map { pref ->
                    pref to prefHolder.name
                }
            }
        }.map { listFlows ->
            combine(flows = listFlows, transform = { listPreferences ->
                listPreferences.map { namePrefPair ->
                    PrefUiModel(
                        name = namePrefPair.second,
                        data = namePrefPair.first.asMap().map { entry ->
                            PrefElement(
                                key = entry.key.toString(),
                                value = entry.value.toString(),
                                type = Type.type(entry.value)
                            )
                        }
                    )
                }
            })
        }.flattenMerge()
    }
}

class PrefUiModel(
    val name: String,
    val data: List<PrefElement>,
    val isExpanded: MutableState<Boolean> = mutableStateOf(true)
)
class PrefElement(val key: String, val value: String, val type: Type)

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

