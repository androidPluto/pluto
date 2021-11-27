package com.mocklets.pluto.logger

import androidx.fragment.app.Fragment
import com.mocklets.pluto.logger.ui.LogsFragment
import com.mocklets.pluto.plugin.Developer
import com.mocklets.pluto.plugin.DeveloperDetails
import com.mocklets.pluto.plugin.Plugin
import com.mocklets.pluto.plugin.PluginConfiguration

class PlutoLoggerPlugin : Plugin() {

    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = "Logger"
    )

    override fun getDeveloperDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            website = "https://pluto.mocklets.com",
            vcsLink = "https://github.com/mocklets/pluto",
            developer = Developer(
                github = "srtvprateek",
                twitter = "srtv_prateek"
            )
        )
    }

    override fun onPluginInstalled() {
    }

    override fun onPluginSelected(): Fragment {
        return LogsFragment()
    }
}
