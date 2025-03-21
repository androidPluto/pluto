package com.pluto.tool

import android.app.Application
import com.pluto.utilities.list.ListItem

/**
 * Base class for all Pluto tools.
 *
 * This abstract class provides the foundation for creating debugging tools in Pluto.
 * Tools are utility features that can be activated directly from the UI, such as
 * rulers, grids, and screen information displays.
 *
 * To create a new tool, extend this class and implement the required abstract methods.
 *
 * @property id A unique string identifier for the tool
 */
internal abstract class PlutoTool(val id: String) : ListItem() {

    /**
     * Returns the tool configuration.
     *
     * This method should provide a ToolConfiguration object that defines
     * the tool's name and icon.
     *
     * @return The tool configuration
     */
    abstract fun getConfig(): ToolConfiguration

    /**
     * Called when the tool is initialized.
     *
     * This method is called during the tool initialization process.
     * It should be used to set up any resources needed by the tool.
     */
    abstract fun onToolInitialised()

    /**
     * Called when the tool is selected by the user.
     *
     * This method is called when the user activates the tool.
     * It should be used to show the tool's UI or start its functionality.
     */
    abstract fun onToolSelected()

    /**
     * Called when the tool is unselected by the user.
     *
     * This method is called when the user deactivates the tool or selects another tool.
     * It should be used to hide the tool's UI or stop its functionality.
     */
    abstract fun onToolUnselected()

    /**
     * Determines whether the tool is enabled.
     *
     * This method should return true if the tool is available for use,
     * or false if it is disabled.
     *
     * @return True if the tool is enabled, false otherwise
     */
    abstract fun isEnabled(): Boolean

    /**
     * The application instance.
     *
     * This property provides access to the application instance for the tool.
     * It throws an IllegalStateException if accessed before the tool is initialized.
     */
    val application: Application
        get() = returnApplication()

    /** The internal application instance, set during initialization */
    private var _application: Application? = null

    /**
     * Returns the application instance.
     *
     * @throws IllegalStateException if the tool is not initialized
     * @return The application instance
     */
    private fun returnApplication(): Application {
        _application?.let {
            return it
        }
        throw IllegalStateException("${this.javaClass.name} plugin is not installed yet.")
    }

    /**
     * Initializes the tool with the provided application instance.
     *
     * This method sets the application instance and calls onToolInitialised().
     *
     * @param application The application instance to use for initialization
     */
    fun initialise(application: Application) {
        this._application = application
        onToolInitialised()
    }
}
