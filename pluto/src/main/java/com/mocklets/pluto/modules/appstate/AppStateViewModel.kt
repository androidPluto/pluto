package com.mocklets.pluto.modules.appstate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mocklets.pluto.Pluto.appProperties

internal class AppStateViewModel : ViewModel() {

    val properties: LiveData<List<AppStateItem>>
        get() = _properties
    private val _properties = MutableLiveData<List<AppStateItem>>()

    fun filter(search: String = "") {
        _properties.postValue(appProperties.convert(search))
    }

    private fun HashMap<String, String?>.convert(search: String): List<AppStateItem> {
        val list = arrayListOf<AppStateItem>()
        forEach { (key, value) ->
            if (key.contains(search, true)) {
                list.add(AppStateItem(key, value))
            }
        }
        return list
    }
}
