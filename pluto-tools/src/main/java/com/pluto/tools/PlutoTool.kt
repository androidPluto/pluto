package com.pluto.tools

import android.app.Application
import android.content.Context
import com.pluto.utilities.list.ListItem

abstract class PlutoTool(val id: String) : ListItem() {

    abstract fun getConfig(): ToolConfiguration
    abstract fun onToolInitialised()
    abstract fun onToolSelected()
    abstract fun onToolUnselected()

    val context: Context
        get() = returnContext()
    private var _application: Application? = null
    private fun returnContext(): Context {
        _application?.let {
            return it.applicationContext
        }
        throw IllegalStateException("${this.javaClass.name} plugin is not installed yet.")
    }

    fun initialise(application: Application) {
        this._application = application
        onToolInitialised()
    }
}
