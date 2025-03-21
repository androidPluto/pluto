package com.pluto.plugin

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.Keep
import androidx.fragment.app.Fragment

/**
 * Base class for all Pluto plugins.
 *
 * This abstract class provides the foundation for creating Pluto debugging plugins.
 * It handles plugin lifecycle, configuration, and UI presentation.
 *
 * To create a new plugin, extend this class and implement the required abstract methods.
 *
 * Example:
 * ```
 * class NetworkPlugin : Plugin("network") {
 *     override fun getConfig() = PluginConfiguration(
 *         name = "Network",
 *         icon = R.drawable.ic_network,
 *         version = "1.0.0"
 *     )
 *
 *     override fun getView() = NetworkFragment()
 *
 *     override fun onPluginInstalled() {
 *         // Initialize plugin resources
 *     }
 *
 *     override fun onPluginDataCleared() {
 *         // Clear plugin data
 *     }
 * }
 * ```
 *
 * @property identifier A unique string identifier for the plugin
 */
@Keep
abstract class Plugin(val identifier: String) : PluginEntity(identifier) {

    /**
     * The application context.
     *
     * This property provides access to the application context for the plugin.
     * It throws an IllegalStateException if accessed before the plugin is installed.
     */
    val context: Context
        get() = returnContext()

    /**
     * The application instance.
     *
     * This property provides access to the application instance for the plugin.
     * It throws an IllegalStateException if accessed before the plugin is installed.
     */
    val application: Application
        get() = returnApplication()

    /** The internal application instance, set during installation */
    private var _application: Application? = null

    /**
     * Returns the application context.
     *
     * @throws IllegalStateException if the plugin is not installed
     * @return The application context
     */
    private fun returnContext(): Context {
        _application?.let {
            return it.applicationContext
        }
        throw IllegalStateException("${this.javaClass.name} plugin is not installed yet.")
    }

    /**
     * Returns the application instance.
     *
     * @throws IllegalStateException if the plugin is not installed
     * @return The application instance
     */
    private fun returnApplication(): Application {
        _application?.let {
            return it
        }
        throw IllegalStateException("${this.javaClass.name} plugin is not installed yet.")
    }

    /**
     * Bundle for saving instance state.
     *
     * This bundle can be used to save and restore the plugin's state.
     */
    var savedInstance: Bundle = Bundle()
        private set

    /**
     * Installs the plugin with the provided application instance.
     *
     * This method is final and cannot be overridden. It sets the application
     * instance and calls onPluginInstalled().
     *
     * @param application The application instance to use for installation
     */
    final override fun install(application: Application) {
        this._application = application
        onPluginInstalled()
    }

    /**
     * Returns the plugin configuration.
     *
     * This method should provide a PluginConfiguration object that defines
     * the plugin's name, icon, and version.
     *
     * @return The plugin configuration
     */
    abstract fun getConfig(): PluginConfiguration

    /**
     * Returns the plugin's UI view.
     *
     * This method should provide a Fragment that implements the plugin's UI.
     *
     * @return The plugin's UI fragment
     */
    abstract fun getView(): Fragment

    /**
     * Returns the plugin developer's details.
     *
     * This method can be overridden to provide information about the plugin's
     * developer, such as VCS link, website, and Twitter handle.
     *
     * @return The developer details, or null if not provided
     */
    open fun getDeveloperDetails(): DeveloperDetails? = null

    /**
     * Called when the plugin is installed.
     *
     * This method is called during the plugin installation process.
     * It should be used to initialize any resources needed by the plugin.
     */
    abstract fun onPluginInstalled()

    /**
     * Called when the plugin's data should be cleared.
     *
     * This method is called when the user requests to clear the plugin's data.
     * It should be used to clear any cached data or logs.
     */
    abstract fun onPluginDataCleared()

    /**
     * Called when the plugin's view is created.
     *
     * This method is called when the plugin's UI is created.
     * It shows a toast message indicating that the view has switched to this plugin.
     *
     * @param savedInstanceState The saved instance state bundle
     */
    @SuppressWarnings("UnusedPrivateMember")
    fun onPluginViewCreated(savedInstanceState: Bundle?) {
        Toast.makeText(context, "View switched to ${getConfig().name}", LENGTH_SHORT).show()
    }
}
