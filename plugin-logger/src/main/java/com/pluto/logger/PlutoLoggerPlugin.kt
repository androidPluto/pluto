package com.pluto.logger

import androidx.fragment.app.Fragment
import com.pluto.logger.internal.LogsFragment
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
import com.pluto.plugin.PluginOption

class PlutoLoggerPlugin : Plugin() {

    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = "Logger",
        icon = R.drawable.pluto_logger___ic_logger_icon
    )

    override fun getView(): Fragment = LogsFragment()

    override fun getOptions(): List<PluginOption> {
        val list = arrayListOf<PluginOption>()
        list.add(PluginOption(id = "clear_logs", label = "Clear Logs", icon = R.drawable.pluto_logger___ic_logger_icon))
        list.add(PluginOption(id = "settings", label = "Settings", icon = R.drawable.pluto_logger___ic_logger_icon))
        return list
    }

    override fun getDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            website = "https://plutolib.com",
            vcsLink = "https://github.com/mocklets/pluto"
        )
    }

    override fun onPluginInstalled() {
    }
}
