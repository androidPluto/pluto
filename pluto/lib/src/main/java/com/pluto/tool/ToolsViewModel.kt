package com.pluto.tool

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.Pluto

/**
 * ViewModel for managing and exposing Pluto tools to the UI.
 *
 * This class provides a LiveData object that contains the list of all available
 * Pluto tools. It is used by the UI to display the list of tools to the user.
 *
 * @param application The application instance
 */
internal class ToolsViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * LiveData containing the list of all available tools.
     *
     * This property provides a read-only view of the tools list for observers.
     */
    val tools: LiveData<List<PlutoTool>>
        get() = _tools

    /**
     * Mutable LiveData containing the list of all available tools.
     *
     * This property is used internally to update the tools list.
     */
    private val _tools = MutableLiveData<List<PlutoTool>>()

    /**
     * Initializes the ViewModel by loading the list of tools from the ToolManager.
     *
     * This method is called when the ViewModel is created.
     */
    init {
        _tools.postValue(
            arrayListOf<PlutoTool>().apply {
                addAll(Pluto.toolManager.tools)
            }
        )
    }
}
