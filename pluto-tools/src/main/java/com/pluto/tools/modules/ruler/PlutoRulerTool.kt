package com.pluto.tools.modules.ruler

import android.content.Intent
import com.pluto.tools.PlutoTool
import com.pluto.tools.R
import com.pluto.tools.ToolConfiguration

class PlutoRulerTool : PlutoTool("ruler") {
    override fun getConfig(): ToolConfiguration = ToolConfiguration(
        name = context.getString(R.string.pluto_tool___ruler_name),
        icon = R.drawable.pluto_tool___ic_ruler_logo,
    )

    override fun onToolInitialised() {
    }

    override fun onToolSelected() {
        val intent = Intent(returnContext().applicationContext, RulerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        returnContext().applicationContext.startActivity(intent)
    }

    override fun onToolUnselected() {
    }
}
