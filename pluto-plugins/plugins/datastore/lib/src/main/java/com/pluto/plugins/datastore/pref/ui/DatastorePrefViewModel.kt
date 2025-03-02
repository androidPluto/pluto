package com.pluto.plugins.datastore.pref.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

    fun setPrefData(pair: DatastorePrefKeyValuePair, data: Any) {
        sharePrefUtils.set(pair, data)
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val list = arrayListOf<DatastorePrefKeyValuePair>()
            getSelectedPrefFiles().forEach {
                it.preferences.data.first().asMap().map { (key, value) ->
                    list.add(
                        DatastorePrefKeyValuePair(key.name, value, it.name).also {
                            _preferences.postValue(listOf(it))
                        }
                    )
                }
            }
            _preferences.postValue(list)
        }
    }
}
