package com.pluto.tools

import android.app.Application
import com.pluto.utilities.list.ListItem

abstract class PlutoTool(val id: String) : ListItem() {

    abstract fun getConfig(): ToolConfiguration
    abstract fun onToolInitialised()
    abstract fun onToolSelected()
    abstract fun onToolUnselected()
    abstract fun isEnabled(): Boolean

    val application: Application
        get() = returnApplication()
    private var _application: Application? = null
    private fun returnApplication(): Application {
        _application?.let {
            return it
        }
        throw IllegalStateException("${this.javaClass.name} plugin is not installed yet.")
    }

    fun initialise(application: Application) {
        this._application = application
        onToolInitialised()
    }
}
