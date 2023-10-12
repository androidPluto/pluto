package com.pluto.plugin

import android.app.Application

abstract class PluginEntity(private val identifier: String) {

    abstract fun install(application: Application)
    override fun equals(other: Any?): Boolean = other is PluginEntity && identifier == other.identifier
    override fun hashCode(): Int = identifier.hashCode()
}
