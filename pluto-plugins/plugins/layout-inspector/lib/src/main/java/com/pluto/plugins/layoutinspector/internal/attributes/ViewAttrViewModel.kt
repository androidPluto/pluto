package com.pluto.plugins.layoutinspector.internal.attributes

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pluto.plugins.layoutinspector.internal.attributes.parser.AttrParser
import com.pluto.plugins.layoutinspector.internal.attributes.parser.Attribute
import kotlinx.coroutines.launch

internal class ViewAttrViewModel(application: Application) : AndroidViewModel(application) {

    private val parser = AttrParser()

    val list: LiveData<List<Attribute>>
        get() = _list
    private val _list = MutableLiveData<List<Attribute>>()

    fun parse(v: View) {
        viewModelScope.launch {
            _list.postValue(parser.parse(v))
        }
    }
}
