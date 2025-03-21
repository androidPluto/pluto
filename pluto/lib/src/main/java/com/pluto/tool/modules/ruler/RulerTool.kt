package com.pluto.tool.modules.ruler

import android.content.Intent
import com.pluto.R
import com.pluto.tool.PlutoTool
import com.pluto.tool.ToolConfiguration

/**
 * A tool that provides a ruler interface for measuring UI elements on the screen.
 *
 * This tool launches a dedicated activity (RulerActivity) that displays a ruler interface
 * with measurement capabilities. The ruler helps developers measure distances, sizes, and
 * alignments of UI elements during app development.
 */
internal class RulerTool : PlutoTool("ruler") {
    /**
     * Provides the configuration for this tool, including its name and icon.
     *
     * @return A ToolConfiguration object with the tool's display properties
     */
    override fun getConfig(): ToolConfiguration = ToolConfiguration(
        name = application.getString(R.string.pluto___tool_ruler_name),
        icon = R.drawable.pluto___tool_ic_ruler_logo,
    )

    /**
     * Called when the tool is initialized.
     * No specific initialization is needed for this tool.
     */
    override fun onToolInitialised() {
    }

    /**
     * Called when the tool is selected by the user.
     * Launches the RulerActivity to display the ruler interface.
     */
    override fun onToolSelected() {
        val intent = Intent(application.applicationContext, RulerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.applicationContext.startActivity(intent)
    }

    /**
     * Called when the tool is unselected by the user.
     * No specific cleanup is needed for this tool as the activity handles its own lifecycle.
     */
    override fun onToolUnselected() {
    }

    /**
     * Determines if this tool is enabled.
     * The ruler tool is always enabled as it doesn't require special permissions.
     *
     * @return Always returns true as this tool is always enabled
     */
    override fun isEnabled(): Boolean = true
}
