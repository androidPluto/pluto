package com.pluto.plugins.datastore.pref.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.plugins.datastore.pref.PreferenceHolder
import com.pluto.plugins.datastore.pref.utils.DatastorePrefUtils

internal class DatastorePrefViewModel(application: Application) : AndroidViewModel(application) {

    val preferenceList: LiveData<List<DatastorePrefKeyValuePair>>
        get() = _preferences
    private val _preferences = MutableLiveData<List<DatastorePrefKeyValuePair>>()

    private val sharePrefUtils = DatastorePrefUtils(application.applicationContext)

    fun refresh() {
        _preferences.postValue(sharePrefUtils.get())
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
}
