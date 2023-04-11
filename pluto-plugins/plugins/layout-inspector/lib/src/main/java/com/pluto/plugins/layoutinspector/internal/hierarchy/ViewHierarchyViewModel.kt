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
        collapseAll(v)
    }

    fun expandAll(rootView: View?) {

    }

    fun collapseAll(rootView: View) {
        viewModelScope.launch {
            val list = arrayListOf(
                Hierarchy(rootView, 0)
            )
            _list.postValue(list)
        }
    }

    fun removeChildren(data: Hierarchy, layoutPosition: Int) {
        viewModelScope.launch {
            val newList = (_list.value ?: arrayListOf()).filterIndexed { index, value ->
                if (index <= layoutPosition) true
                else value.layerCount <= data.layerCount
            }
            val list = arrayListOf<Hierarchy>().apply {
                addAll(newList)
            }
            list[layoutPosition] = Hierarchy(
                view = data.view,
                layerCount = data.layerCount,
                isExpanded = false
            )
            _list.postValue(list)
        }
    }

    fun addChildren(data: Hierarchy, layoutPosition: Int) {
        viewModelScope.launch {
            val children = data.assembleChildren()
            val list = _list.value ?: arrayListOf()

            list[layoutPosition] = Hierarchy(
                view = data.view,
                layerCount = data.layerCount,
                isExpanded = true
            )
            list.addAll(layoutPosition + 1, children)
            _list.postValue(list)
        }
    }
}
