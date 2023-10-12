package com.pluto.ui.selector

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.pluto.plugin.PluginGroup
import com.pluto.utilities.SingleLiveEvent

internal class PluginsGroupViewModel(application: Application) : AndroidViewModel(application) {

    val current: LiveData<PluginGroup>
        get() = _current
    private val _current = SingleLiveEvent<PluginGroup>()

    fun set(group: PluginGroup) {
        _current.postValue(group)
    }
}
