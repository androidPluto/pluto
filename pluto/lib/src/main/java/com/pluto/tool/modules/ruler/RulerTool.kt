package com.pluto.tool.modules.ruler

import android.content.Intent
import com.pluto.R
import com.pluto.tool.PlutoTool
import com.pluto.tool.ToolConfiguration

internal class RulerTool : PlutoTool("ruler") {
    override fun getConfig(): ToolConfiguration = ToolConfiguration(
        name = application.getString(R.string.pluto___tool_ruler_name),
        icon = R.drawable.pluto___tool_ic_ruler_logo,
    )

    override fun onToolInitialised() {
    }

    override fun onToolSelected() {
        val intent = Intent(application.applicationContext, RulerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.applicationContext.startActivity(intent)
    }

    override fun onToolUnselected() {
    }

    override fun isEnabled(): Boolean = true
}
