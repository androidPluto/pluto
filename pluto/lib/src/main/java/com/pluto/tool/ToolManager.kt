package com.pluto.tool

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.pluto.core.applifecycle.AppStateCallback
import com.pluto.tool.modules.currentScreen.CurrentScreenTool
import com.pluto.tool.modules.grid.GridViewTool
import com.pluto.tool.modules.ruler.RulerTool

/**
 * Manages the initialization and interaction with Pluto tools.
 *
 * This class is responsible for initializing tools, retrieving tools by identifier,
 * and handling tool selection. It maintains a registry of all available tools.
 *
 * @property application The application instance used for tool initialization
 * @param state LiveData that emits application state changes
 */
internal class ToolManager(private val application: Application, state: MutableLiveData<AppStateCallback.State>) {

    /**
     * Set of all available tools.
     *
     * This set contains all the tools that are available in Pluto.
     * Tools are initialized when the ToolManager is initialized.
     */
    val tools: LinkedHashSet<PlutoTool> = linkedSetOf<PlutoTool>().apply {
        add(RulerTool())
        add(GridViewTool())
        add(CurrentScreenTool())
//        add(ScreenHistoryTool())
    }

    /**
     * Initializes the tool manager by observing app state changes.
     *
     * When the app goes to the background, all tools are unselected.
     */
    init {
        state.observeForever {
            if (it is AppStateCallback.State.Background) {
                tools.forEach { tool ->
                    tool.onToolUnselected()
                }
            }
        }
    }

    /**
     * Initializes all tools with the application instance.
     *
     * This method is called during Pluto initialization to set up all tools.
     */
    fun initialise() {
        tools.forEach {
            it.initialise(application)
        }
    }

    /**
     * Retrieves a tool by its identifier.
     *
     * @param identifier The unique identifier of the tool to retrieve
     * @return The tool with the specified identifier, or null if not found
     */
    fun get(identifier: String): PlutoTool? {
        return tools.firstOrNull {
            it.id == identifier
        }
    }

    /**
     * Selects a tool by its identifier.
     *
     * This method calls the onToolSelected method of the specified tool.
     *
     * @param id The identifier of the tool to select
     */
    fun select(id: String) {
        get(id)?.onToolSelected()
    }
}
