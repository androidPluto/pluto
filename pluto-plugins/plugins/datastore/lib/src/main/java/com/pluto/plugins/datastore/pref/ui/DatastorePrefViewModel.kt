package com.pluto.plugins.datastore.pref.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.plugins.datastore.pref.PreferenceHolder
import com.pluto.plugins.datastore.pref.utils.DatastorePrefKeyValuePair
import com.pluto.plugins.datastore.pref.utils.DatastorePrefUtils

internal class DatastorePrefViewModel(application: Application) : AndroidViewModel(application) {

    val preferenceList: LiveData<List<DatastorePrefKeyValuePair>>
        get() = _preferences
    private val _preferences = MutableLiveData<List<DatastorePrefKeyValuePair>>()

    private val sharePrefUtils = DatastorePrefUtils(application.applicationContext)

    fun refresh() {
        _preferences.postValue(retrieveAllPreferenceData() ?: emptyList())
    }

    fun getPrefFiles(): List<PreferenceHolder> = sharePrefUtils.allPreferenceFiles

    fun getSelectedPrefFiles(): List<PreferenceHolder> = sharePrefUtils.selectedPreferenceFiles

    fun setSelectedPrefFiles(files: List<PreferenceHolder>) {
        sharePrefUtils.selectedPreferenceFiles = files
        refresh()
    }

    fun setPrefData(pair: DatastorePrefKeyValuePair, data: Any) {
        sharePrefUtils.set(pair, data)
        refresh()
    }

    private fun retrieveAllPreferenceData(): List<DatastorePrefKeyValuePair>? {
//        viewModelScope.launch {
//            DebugLog.e("prateek", PlutoDatastoreWatcher.sources.value.size.toString())
//            PlutoDatastoreWatcher.sources.map { source ->
//                source.map { prefHolder ->
//                    prefHolder.preferences.data
//                        .catch { exception ->
//                            DebugLog.e("prateek", "${prefHolder.name} : ${exception.message}")
//                            // DataStore calls can throw an IOException when an error is encountered when reading data
//                            if (exception is IOException) {
//                                emit(emptyPreferences())
//                            } else {
//                                throw exception
//                            }
//                        }
//                        .map { preferences ->
//                            // Map Preferences to a Map<String, Any?>
//                            preferences.asMap().mapKeys { it.key.name }.toMap()
//                        }.collect {
//                            DebugLog.e("prateek", "${prefHolder.name} : $it")
//                        }
//                }
//
// //                source.preferences.data
// //                    .catch { exception ->
// //                        DebugLog.e("prateek", "${source.name} : ${exception.message}")
// //                        // DataStore calls can throw an IOException when an error is encountered when reading data
// //                        if (exception is IOException) {
// //                            emit(emptyPreferences())
// //                        } else {
// //                            throw exception
// //                        }
// //                    }
// //                    .map { preferences ->
// //                        // Map Preferences to a Map<String, Any?>
// //                        preferences.asMap().mapKeys { it.key.name }.toMap()
// //                    }.collect {
// //                        DebugLog.e("prateek", "${source.name} : $it")
// //                    }
//            }.map { listFlows ->
//                combine(
//                    flows = listFlows,
//                    transform = { listPreferences ->
//                    }
//                )
//            }
//        }
        return emptyList()
    }
}
