package com.pluto.tool

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.Pluto
import com.pluto.tools.PlutoTool

internal class ToolsViewModel(application: Application) : AndroidViewModel(application) {

    val tools: LiveData<List<PlutoTool>>
        get() = _tools
    private val _tools = MutableLiveData<List<PlutoTool>>()

    init {
        _tools.postValue(
            arrayListOf<PlutoTool>().apply {
                addAll(Pluto.toolManager.tools)
            }
        )
    }
}
