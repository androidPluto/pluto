package com.mocklets.pluto.modules.preferences.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mocklets.pluto.modules.preferences.SharedPrefRepo

internal class SharedPrefViewModel(application: Application) : AndroidViewModel(application) {

    val preferences: LiveData<List<SharedPrefKeyValuePair>>
        get() = _preferences
    private val _preferences = MutableLiveData<List<SharedPrefKeyValuePair>>()

    fun refresh() {
        _preferences.postValue(SharedPrefRepo.get(getApplication()))
    }
}
