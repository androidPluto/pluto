package com.pluto.tools.modules.screenHistory

import com.pluto.tools.PlutoTool
import com.pluto.tools.R
import com.pluto.tools.ToolConfiguration

internal class ScreenHistoryTool : PlutoTool("screenHistory") {
    override fun getConfig(): ToolConfiguration = ToolConfiguration(
        name = application.getString(R.string.pluto_tool___screen_history_name),
        icon = R.drawable.pluto_tool___ic_screen_history_logo
    )

    override fun onToolInitialised() {
    }

    override fun onToolSelected() {
    }

    override fun onToolUnselected() {
    }

    override fun isEnabled(): Boolean = true
}
