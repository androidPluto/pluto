package com.pluto.plugin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.pluto.plugin.utilities.SingleLiveEvent

@Deprecated("global level plugin options are no longer supported")
class PluginOptionsViewModel(application: Application) : AndroidViewModel(application) {

    val currentOption: LiveData<String>
        get() = _currentOption
    private val _currentOption = SingleLiveEvent<String>()

    fun select(id: String) {
        _currentOption.postValue(id)
    }
}
