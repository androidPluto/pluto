package com.pluto.tool.modules.screenHistory

import com.pluto.R
import com.pluto.tool.PlutoTool
import com.pluto.tool.ToolConfiguration

/**
 * A tool that provides screen history tracking functionality.
 *
 * This tool is designed to track and display the history of screens (activities and fragments)
 * that the user has navigated through in the application. It helps developers understand
 * the navigation flow and debug navigation-related issues.
 *
 * Note: This appears to be a placeholder implementation with minimal functionality.
 * The actual screen history tracking logic would need to be implemented in the
 * onToolSelected method.
 */
internal class ScreenHistoryTool : PlutoTool("screenHistory") {
    /**
     * Provides the configuration for this tool, including its name and icon.
     *
     * @return A ToolConfiguration object with the tool's display properties
     */
    override fun getConfig(): ToolConfiguration = ToolConfiguration(
        name = application.getString(R.string.pluto___tool_screen_history_name),
        icon = R.drawable.pluto___tool_ic_screen_history_logo
    )

    /**
     * Called when the tool is initialized.
     * No specific initialization is implemented for this tool.
     */
    override fun onToolInitialised() {
    }

    /**
     * Called when the tool is selected by the user.
     * This would be where the screen history display would be triggered,
     * but the implementation is currently empty.
     */
    override fun onToolSelected() {
    }

    /**
     * Called when the tool is unselected by the user.
     * No specific cleanup is implemented for this tool.
     */
    override fun onToolUnselected() {
    }

    /**
     * Determines if this tool is enabled.
     * The screen history tool is always enabled as it doesn't require special permissions.
     *
     * @return Always returns true as this tool is always enabled
     */
    override fun isEnabled(): Boolean = true
}
