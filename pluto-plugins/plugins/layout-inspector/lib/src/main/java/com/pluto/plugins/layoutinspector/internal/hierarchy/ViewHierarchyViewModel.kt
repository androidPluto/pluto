package com.pluto.plugins.layoutinspector.internal.hierarchy

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

internal class ViewHierarchyViewModel(application: Application) : AndroidViewModel(application) {

    val list: LiveData<ArrayList<Hierarchy>>
        get() = _list
    private val _list = MutableLiveData<ArrayList<Hierarchy>>()

    fun parse(v: View) {
        viewModelScope.launch {
            val list = arrayListOf(
                Hierarchy(v, 0),
                Hierarchy(v, 1),
                Hierarchy(v, 2)
            )
            _list.postValue(list)
        }
    }
}
