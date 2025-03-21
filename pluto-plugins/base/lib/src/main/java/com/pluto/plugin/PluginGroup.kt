package com.pluto.plugin

import android.app.Application

/**
 * Base class for grouping related Pluto plugins.
 *
 * This abstract class allows multiple plugins to be grouped together under a
 * common identifier. Plugin groups are useful for organizing related plugins,
 * such as database inspection tools or network monitoring utilities.
 *
 * To create a new plugin group, extend this class and implement the required
 * abstract methods.
 *
 * Example:
 * ```
 * class DatabasePluginGroup : PluginGroup("database") {
 *     override fun getConfig() = PluginGroupConfiguration(
 *         name = "Database Tools"
 *     )
 *
 *     override fun getPlugins() = listOf(
 *         RoomDatabasePlugin(),
 *         SharedPreferencesPlugin()
 *     )
 * }
 * ```
 *
 * @param identifier A unique string identifier for the plugin group
 */
abstract class PluginGroup(identifier: String) : PluginEntity(identifier) {

    /** Set of installed plugins in this group */
    private var plugins: LinkedHashSet<Plugin> = linkedSetOf()

    /**
     * List of all installed plugins in this group.
     *
     * This property returns a copy of the internal plugins set as a list,
     * ensuring that the original set cannot be modified externally.
     */
    val installedPlugins: List<Plugin>
        get() {
            val list = arrayListOf<Plugin>()
            list.addAll(plugins)
            return list
        }

    /**
     * Returns the plugin group configuration.
     *
     * This method should provide a PluginGroupConfiguration object that defines
     * the group's name and icon.
     *
     * @return The plugin group configuration
     */
    abstract fun getConfig(): PluginGroupConfiguration

    /**
     * Returns the list of plugins in this group.
     *
     * This method should provide a list of all plugins that belong to this group.
     *
     * @return The list of plugins in this group
     */
    protected abstract fun getPlugins(): List<Plugin>

    /**
     * Installs all plugins in this group with the provided application instance.
     *
     * This method is final and cannot be overridden. It installs each plugin
     * in the group and adds it to the internal registry of plugins.
     *
     * @param application The application instance to use for installation
     */
    final override fun install(application: Application) {
        getPlugins().forEach {
            it.install(application)
            plugins.add(it)
        }
    }
}
