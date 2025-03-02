package com.pluto.plugins.datastore.pref.ui

import android.app.Application
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pluto.plugins.datastore.pref.PlutoDatastoreWatcher
import com.pluto.plugins.datastore.pref.PreferenceHolder
import com.pluto.plugins.datastore.pref.utils.DatastorePrefKeyValuePair
import com.pluto.plugins.datastore.pref.utils.DatastorePrefUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal class DatastorePrefViewModel(application: Application) : AndroidViewModel(application) {

    val preferenceList: LiveData<List<DatastorePrefKeyValuePair>>
        get() = _preferences
    private val _preferences = MutableLiveData<List<DatastorePrefKeyValuePair>>()

    private val sharePrefUtils = DatastorePrefUtils(application.applicationContext)

    fun getSelectedPrefFiles(): List<PreferenceHolder> = sharePrefUtils.selectedPreferenceFiles

    fun setSelectedPrefFiles(files: List<PreferenceHolder>) {
        sharePrefUtils.selectedPreferenceFiles = files
        refresh()
    }

    fun setPrefData(pair: DatastorePrefKeyValuePair, value: Any) {
        viewModelScope.launch {
            val preferences = PlutoDatastoreWatcher.sources.value.find {
                it.name == pair.prefLabel
            }?.preferences

            preferences?.edit { preference ->
                when (pair.value) {
                    is Boolean -> preference[booleanPreferencesKey(pair.key)] = value as Boolean
                    is Double -> preference[doublePreferencesKey(pair.key)] = value as Double
                    is Int -> preference[intPreferencesKey(pair.key)] = value as Int
                    is Float -> preference[floatPreferencesKey(pair.key)] = value as Float
                    is Long -> preference[longPreferencesKey(pair.key)] = value as Long
                    is String -> preference[stringPreferencesKey(pair.key)] = value as String
                    else -> {
                        // show some error
                        // add validation before sending data here
                    }
                }
            }
            refresh()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            val list = arrayListOf<DatastorePrefKeyValuePair>()
            getSelectedPrefFiles().forEach {
                it.preferences.data.first().asMap().map { (key, value) ->
                    list.add(DatastorePrefKeyValuePair(key.name, value, it.name))
                }
            }
            _preferences.postValue(list)
        }
    }
}
