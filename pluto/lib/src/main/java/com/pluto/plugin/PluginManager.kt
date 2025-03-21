package com.pluto.plugin

import android.app.Application
import com.pluto.plugin.libinterface.PlutoInterface
import com.pluto.ui.container.PlutoActivity
import com.pluto.ui.selector.SelectorActivity

/**
 * Manages the installation and interaction with Pluto plugins.
 *
 * This class is responsible for installing plugins, retrieving plugins by identifier,
 * and clearing plugin logs. It maintains a registry of all installed plugins and
 * plugin groups.
 *
 * @property application The application instance used for plugin installation
 */
internal class PluginManager(private val application: Application) {

    /** Set of all installed plugins and plugin groups */
    private var plugins: LinkedHashSet<PluginEntity> = linkedSetOf()

    /**
     * List of all installed plugins and plugin groups.
     *
     * This property returns a copy of the internal plugins set as a list,
     * ensuring that the original set cannot be modified externally.
     */
    internal val installedPlugins: List<PluginEntity>
        get() {
            val list = arrayListOf<PluginEntity>()
            list.addAll(plugins)
            return list
        }

    /**
     * Initializes the plugin manager by creating the Pluto interface.
     *
     * The Pluto interface provides a bridge between plugins and the main Pluto library.
     */
    init {
        PlutoInterface.create(
            application = application,
            pluginActivityClass = PlutoActivity::class.java,
            selectorActivityClass = SelectorActivity::class.java
        )
    }

    /**
     * Installs a set of plugins or plugin groups.
     *
     * Each plugin or plugin group is installed by calling its install method
     * and then added to the internal registry of plugins.
     *
     * @param plugins The set of plugins or plugin groups to install
     */
    fun install(plugins: LinkedHashSet<PluginEntity>) {
        plugins.forEach {
            it.install(application)
            this.plugins.add(it)
        }
    }

    /**
     * Retrieves a plugin by its identifier.
     *
     * This method searches through all installed plugins and plugin groups
     * to find a plugin with the specified identifier.
     *
     * @param identifier The unique identifier of the plugin to retrieve
     * @return The plugin with the specified identifier, or null if not found
     */
    fun get(identifier: String): Plugin? {
        plugins.forEach {
            when (it) {
                is Plugin -> if (it.identifier == identifier) return it
                is PluginGroup -> return it.installedPlugins.firstOrNull { plugin -> plugin.identifier == identifier }
            }
        }
        return null
    }

    /**
     * Clears logs for a specific plugin or all plugins.
     *
     * If an identifier is provided, only the logs for that plugin are cleared.
     * If no identifier is provided, logs for all plugins are cleared.
     *
     * @param identifier The identifier of the plugin to clear logs for, or null to clear all logs
     */
    fun clearLogs(identifier: String? = null) {
        identifier?.let { get(identifier)?.onPluginDataCleared() } ?: run { clearAllLogs() }
    }

    /**
     * Clears logs for all installed plugins and plugin groups.
     *
     * This method iterates through all installed plugins and plugin groups
     * and calls their onPluginDataCleared method.
     */
    private fun clearAllLogs() {
        installedPlugins.forEach {
            when (it) {
                is Plugin -> it.onPluginDataCleared()
                is PluginGroup -> it.installedPlugins.forEach { plugin -> plugin.onPluginDataCleared() }
            }
        }
    }
}
