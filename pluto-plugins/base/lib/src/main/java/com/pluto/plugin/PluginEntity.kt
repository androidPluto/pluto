package com.pluto.plugin

import com.pluto.utilities.list.ListItem

abstract class PluginEntity(private val identifier: String) : ListItem() {
    override fun equals(other: Any?): Boolean = other is PluginEntity && identifier == other.identifier
    override fun hashCode(): Int = identifier.hashCode()
}
