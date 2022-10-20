package com.pluto.tool.modules.screenHistory

import com.pluto.R
import com.pluto.tool.PlutoTool
import com.pluto.tool.ToolConfiguration

internal class ScreenHistoryTool : PlutoTool("screenHistory") {
    override fun getConfig(): ToolConfiguration = ToolConfiguration(
        name = application.getString(R.string.pluto___tool_screen_history_name),
        icon = R.drawable.pluto___tool_ic_screen_history_logo
    )

    override fun onToolInitialised() {
    }

    override fun onToolSelected() {
    }

    override fun onToolUnselected() {
    }

    override fun isEnabled(): Boolean = true
}
