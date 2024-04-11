package com.pluto.plugins.logger

import androidx.fragment.app.Fragment
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
import com.pluto.plugins.logger.internal.LogsFragment
import com.pluto.plugins.logger.internal.persistence.LogDBHandler

class PlutoLoggerPlugin() : Plugin(ID) {

    @SuppressWarnings("UnusedPrivateMember")
    @Deprecated("Use the default constructor PlutoLoggerPlugin() instead.")
    constructor(identifier: String) : this()

    private val settingsPreferences by lazy { Preferences(application) }

    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = context.getString(R.string.pluto_logger___plugin_name),
        icon = R.drawable.pluto_logger___ic_logger_icon,
        version = BuildConfig.VERSION_NAME
    )

    override fun getView(): Fragment = LogsFragment()

    override fun getDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            website = "https://androidpluto.com",
            vcsLink = "https://github.com/androidPluto/pluto",
            twitter = "https://twitter.com/android_pluto"
        )
    }

    override fun onPluginInstalled() {
        LogDBHandler.initialize(context)
    }

    override fun onPluginDataCleared() {
        LogDBHandler.flush()
        settingsPreferences.clearFilters()
    }

    companion object {
        const val ID = "logger"
    }
}
