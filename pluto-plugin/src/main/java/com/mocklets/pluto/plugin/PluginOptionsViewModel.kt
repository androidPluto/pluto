package com.mocklets.pluto.plugin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.mocklets.pluto.utilities.SingleLiveEvent

class PluginOptionsViewModel(application: Application) : AndroidViewModel(application) {

    val currentOption: LiveData<String>
        get() = _currentOption
    private val _currentOption = SingleLiveEvent<String>()

    fun select(id: String) {
        _currentOption.postValue(id)
    }
}
