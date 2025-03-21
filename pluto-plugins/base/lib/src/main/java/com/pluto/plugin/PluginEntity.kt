package com.pluto.plugin

import android.app.Application

/**
 * Base class for all Pluto plugin entities.
 *
 * This abstract class serves as the foundation for both individual plugins and plugin groups.
 * It provides a common interface for installation and identity management.
 *
 * @property identifier A unique string identifier for the plugin entity
 */
abstract class PluginEntity(private val identifier: String) {

    /**
     * Installs the plugin entity with the provided application instance.
     *
     * This method is called during Pluto initialization to set up the plugin.
     *
     * @param application The application instance to use for installation
     */
    abstract fun install(application: Application)

    /**
     * Compares this plugin entity with another object for equality.
     *
     * Plugin entities are considered equal if they have the same identifier.
     *
     * @param other The object to compare with
     * @return True if the objects are equal, false otherwise
     */
    override fun equals(other: Any?): Boolean = other is PluginEntity && identifier == other.identifier

    /**
     * Returns a hash code value for this plugin entity.
     *
     * The hash code is based on the identifier to ensure consistency with equals.
     *
     * @return The hash code value
     */
    override fun hashCode(): Int = identifier.hashCode()
}
