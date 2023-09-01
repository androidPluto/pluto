package com.pluto.plugin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.Pluto

internal class PluginsViewModel(application: Application) : AndroidViewModel(application) {

    val plugins: LiveData<List<PluginEntity>>
        get() = _plugins
    private val _plugins = MutableLiveData<List<PluginEntity>>()

    init {
        _plugins.postValue(Pluto.pluginManager.installedPlugins)
    }
}
