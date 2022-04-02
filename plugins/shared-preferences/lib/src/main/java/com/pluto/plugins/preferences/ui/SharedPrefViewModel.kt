package com.pluto.plugins.preferences.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.plugins.preferences.SharedPrefRepo

internal class SharedPrefViewModel(application: Application) : AndroidViewModel(application) {

    val preferences: LiveData<List<SharedPrefKeyValuePair>>
        get() = _preferences
    private val _preferences = MutableLiveData<List<SharedPrefKeyValuePair>>()

    val current: LiveData<SharedPrefKeyValuePair>
        get() = _current
    private val _current = MutableLiveData<SharedPrefKeyValuePair>()

    internal fun updateCurrentPref(data: SharedPrefKeyValuePair) {
        _current.postValue(data)
    }

    fun refresh() {
        _preferences.postValue(SharedPrefRepo.get(getApplication()))
    }
}
