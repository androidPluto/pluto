package com.pluto.tools.modules.currentView

import com.pluto.tools.PlutoTool
import com.pluto.tools.R
import com.pluto.tools.ToolConfiguration

internal class PlutoCurrentViewTool : PlutoTool("currentView") {
    override fun getConfig(): ToolConfiguration = ToolConfiguration(
        name = application.getString(R.string.pluto_tool___current_view_name),
        icon = R.drawable.pluto_tool___ic_current_view_logo,
    )

    override fun onToolInitialised() {
    }

    override fun onToolSelected() {
    }

    override fun onToolUnselected() {
    }
}
