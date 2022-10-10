package com.pluto.tools.modules.grid

import com.pluto.tools.PlutoTool
import com.pluto.tools.R
import com.pluto.tools.ToolConfiguration

internal class PlutoGridTool : PlutoTool("grid") {

    private lateinit var gridView: GridView

    override fun getConfig(): ToolConfiguration = ToolConfiguration(
        name = context.getString(R.string.pluto_tool___grid_name),
        icon = R.drawable.pluto_tool___ic_grid_logo,
    )

    override fun onToolInitialised() {
        gridView = GridView(returnContext())
    }

    override fun onToolSelected() {
        gridView.toggle()
    }

    override fun onToolUnselected() {
    }
}
