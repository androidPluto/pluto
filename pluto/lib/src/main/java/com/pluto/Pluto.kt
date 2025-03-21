package com.pluto

import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.pluto.core.Session
import com.pluto.core.applifecycle.AppLifecycle
import com.pluto.core.applifecycle.AppStateCallback
import com.pluto.core.notch.Notch
import com.pluto.core.notch.NotchStateCallback
import com.pluto.core.notification.NotificationManager
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginEntity
import com.pluto.plugin.PluginGroup
import com.pluto.plugin.PluginManager
import com.pluto.plugin.libinterface.NotificationInterface.Companion.BUNDLE_LABEL
import com.pluto.plugin.libinterface.NotificationInterface.Companion.ID_LABEL
import com.pluto.plugin.settings.SettingsPreferences
import com.pluto.settings.ResetDataCallback
import com.pluto.tool.ToolManager
import com.pluto.ui.container.PlutoActivity
import com.pluto.ui.selector.SelectorActivity
import com.pluto.ui.selector.SelectorStateCallback
import com.pluto.utilities.extensions.toast

/**
 * Main entry point for the Pluto debugging library.
 *
 * Pluto is a singleton object that provides access to various debugging tools and plugins.
 * It must be initialized with an Application instance and a set of plugins using the [Installer] class.
 *
 * Example usage:
 * ```
 * Pluto.Installer(application)
 *     .addPlugin(NetworkPlugin())
 *     .addPluginGroup(DatabasePluginGroup())
 *     .install()
 * ```
 *
 * Once initialized, Pluto can be opened using the [open] method, which will display either
 * a specific plugin or the plugin selector screen.
 */
object Pluto {

    /** Activity lifecycle callback handler to track app state */
    private lateinit var appLifecycle: AppLifecycle

    /** Application instance used for context and lifecycle callbacks */
    private lateinit var application: Application

    /** Optional notch UI component that can be shown/hidden */
    private var notch: Notch? = null

    /** Manages all installed plugins */
    internal lateinit var pluginManager: PluginManager

    /** Manages debugging tools */
    internal lateinit var toolManager: ToolManager

    /** Manages notifications */
    private lateinit var notificationManager: NotificationManager

    /** Maintains the current debugging session */
    internal val session = Session()

    /** Callback for data reset operations */
    internal lateinit var resetDataCallback: ResetDataCallback

    /** Callback for app state changes (foreground/background) */
    internal lateinit var appStateCallback: AppStateCallback

    /** Callback for selector UI state changes */
    internal lateinit var selectorStateCallback: SelectorStateCallback

    /** Callback for notch state changes */
    private lateinit var notchStateCallback: NotchStateCallback

    /**
     * Initializes Pluto with the application instance and a set of plugins.
     *
     * This method:
     * 1. Initializes all callbacks
     * 2. Registers activity lifecycle callbacks
     * 3. Installs plugins
     * 4. Initializes tools
     * 5. Sets up notifications
     * 6. Initializes settings
     * 7. Sets up the notch UI component
     *
     * @param application The application instance
     * @param plugins The set of plugins to install
     */
    private fun init(application: Application, plugins: LinkedHashSet<PluginEntity>) {
        initialiseCallbacks()
        this.application = application
        appLifecycle = AppLifecycle(appStateCallback)
        application.registerActivityLifecycleCallbacks(appLifecycle)
        pluginManager = PluginManager(application).apply {
            install(plugins)
        }
        toolManager = ToolManager(application, appStateCallback.state).apply {
            initialise()
        }
        notificationManager = NotificationManager(application, appStateCallback.state)
        SettingsPreferences.init(application.applicationContext)
        notch = Notch(application, notchStateCallback.state)
    }

    /**
     * Opens Pluto UI, either showing a specific plugin or the plugin selector screen.
     *
     * If an identifier is provided, Pluto will attempt to open the corresponding plugin.
     * If the plugin is not found, a toast message will be shown.
     * If no identifier is provided, the plugin selector screen will be shown.
     *
     * @param identifier The plugin identifier to open, or null to show the plugin selector
     * @param bundle Optional bundle of data to pass to the plugin
     */
    @JvmOverloads
    fun open(identifier: String? = null, bundle: Bundle? = null) {
        val intent: Intent?
        if (identifier != null) {
            pluginManager.get(identifier)?.let {
                intent = Intent(application.applicationContext, PlutoActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                intent.putExtra(ID_LABEL, identifier)
                intent.putExtra(BUNDLE_LABEL, bundle)
                application.applicationContext.startActivity(intent)
                return
            }
            application.applicationContext.toast("Plugin [$identifier] not installed")
        } else {
            intent = Intent(application.applicationContext, SelectorActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            application.applicationContext.startActivity(intent)
        }
    }

    /**
     * Shows or hides the notch UI component.
     *
     * The notch is a small UI element that can be used to quickly access Pluto.
     *
     * @param state True to show the notch, false to hide it
     */
    fun showNotch(state: Boolean) {
        notch?.enable(state)
    }

    /**
     * Clears logs for a specific plugin or all plugins.
     *
     * @param identifier The plugin identifier to clear logs for, or null to clear logs for all plugins
     */
    @JvmOverloads
    fun clearLogs(identifier: String? = null) {
        pluginManager.clearLogs(identifier)
    }

    /**
     * Initializes all callbacks used by Pluto.
     *
     * This includes:
     * - Reset data callback
     * - App state callback
     * - Selector state callback
     * - Notch state callback
     */
    private fun initialiseCallbacks() {
        resetDataCallback = ResetDataCallback()
        appStateCallback = AppStateCallback()
        selectorStateCallback = SelectorStateCallback()
        notchStateCallback = NotchStateCallback(appStateCallback.state, selectorStateCallback.state)
    }

    /**
     * Builder class for initializing Pluto with plugins.
     *
     * This class provides a fluent API for adding plugins and plugin groups
     * before installing Pluto.
     *
     * @property application The application instance to initialize Pluto with
     */
    class Installer(private val application: Application) {

        private val plugins = linkedSetOf<PluginEntity>()

        /**
         * Adds a plugin to be installed with Pluto.
         *
         * @param plugin The plugin to add
         * @return This Installer instance for method chaining
         */
        fun addPlugin(plugin: Plugin): Installer {
            plugins.add(plugin)
            return this
        }

        /**
         * Adds a plugin group to be installed with Pluto.
         *
         * A plugin group is a collection of related plugins.
         *
         * @param pluginGroup The plugin group to add
         * @return This Installer instance for method chaining
         */
        fun addPluginGroup(pluginGroup: PluginGroup): Installer {
            plugins.add(pluginGroup)
            return this
        }

        /**
         * Completes the installation process by initializing Pluto with the added plugins.
         *
         * This method should be called after all plugins have been added.
         */
        fun install() {
            init(application, plugins)
        }
    }
}
