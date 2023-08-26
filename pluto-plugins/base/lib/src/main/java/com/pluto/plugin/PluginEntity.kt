package com.pluto.plugin

import android.app.Application
import com.pluto.utilities.list.ListItem

abstract class PluginEntity(private val identifier: String) : ListItem() {

    abstract fun install(application: Application)
    override fun equals(other: Any?): Boolean = other is PluginEntity && identifier == other.identifier
    override fun hashCode(): Int = identifier.hashCode()
}
