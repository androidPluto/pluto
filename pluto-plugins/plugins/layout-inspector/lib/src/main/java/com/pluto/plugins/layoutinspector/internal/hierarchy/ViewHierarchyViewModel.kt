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

    fun parseInit(v: View) {
        viewModelScope.launch {
            val list = arrayListOf(
                Hierarchy(v, 0)
            )
            _list.postValue(list)
        }
    }

    fun expandAll(rootView: View?) {

    }

    fun collapseAll(rootView: View) {

    }

    fun removeChildren(data: Hierarchy, layoutPosition: Int) {
//        val children = data.assembleChildren()
//        _list.value?.addAll(layoutPosition+1, children)
    }

    fun addChildren(data: Hierarchy, layoutPosition: Int) {
        val children = data.assembleChildren()
        val list = _list.value ?: arrayListOf()
        list[layoutPosition].isExpanded = true
        list.addAll(layoutPosition + 1, children)
        _list.postValue(list)
    }
}
