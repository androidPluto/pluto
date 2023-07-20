package com.pluto.plugin

abstract class PluginGroup(identifier: String) : PluginEntity(identifier) {

    abstract fun getConfig(): PluginGroupConfiguration

    abstract fun getPlugins(): List<Plugin>
}
