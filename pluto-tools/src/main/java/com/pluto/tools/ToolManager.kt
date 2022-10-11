package com.pluto.tools

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.pluto.tools.modules.currentView.PlutoCurrentViewTool
import com.pluto.tools.modules.grid.PlutoGridTool
import com.pluto.tools.modules.ruler.PlutoRulerTool
import com.pluto.tools.modules.screenHistory.PlutoScreenHistoryTool
import com.pluto.utilities.AppState

class ToolManager {

    var tools: LinkedHashSet<PlutoTool> = linkedSetOf<PlutoTool>().apply {
        add(PlutoRulerTool())
        add(PlutoGridTool())
        add(PlutoCurrentViewTool())
        add(PlutoScreenHistoryTool())
    }

    fun initialise(application: Application, state: MutableLiveData<AppState>) {
        state.observeForever {
            if (it is AppState.Background) {
                tools.forEach { tool ->
                    tool.onToolUnselected()
                }
            }
        }
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
