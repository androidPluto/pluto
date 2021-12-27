package com.pluto.plugin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.Pluto

class PluginsViewModel(application: Application) : AndroidViewModel(application) {

    val plugins: LiveData<List<Plugin>>
        get() = _plugins
    private val _plugins = MutableLiveData<List<Plugin>>()

    init {
        _plugins.postValue(Pluto.pluginManager.installedPlugins)
    }
}
