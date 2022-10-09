package com.pluto.tools

import android.app.Application
import com.pluto.tools.modules.ruler.PlutoRulerTool

class ToolManager {

    var tools: LinkedHashSet<PlutoTool> = linkedSetOf<PlutoTool>().apply {
        add(PlutoRulerTool())
    }

    fun initialise(application: Application) {
        tools.forEach {
            it.initialise(application)
        }
    }

    fun get(identifier: String): PlutoTool? {
        return tools.firstOrNull {
            it.id == identifier
        }
    }

    fun select(id: String) {
        get(id)?.onToolSelected()
    }
}
