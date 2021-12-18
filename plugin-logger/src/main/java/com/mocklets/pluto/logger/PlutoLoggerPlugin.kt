package com.mocklets.pluto.logger

import android.content.Context
import androidx.fragment.app.Fragment
import com.mocklets.pluto.plugin.Developer
import com.mocklets.pluto.plugin.DeveloperDetails
import com.mocklets.pluto.plugin.Plugin
import com.mocklets.pluto.plugin.PluginConfiguration

class PlutoLoggerPlugin : Plugin() {

    override fun getConfig(): PluginConfiguration = PluginConfiguration(
        name = "Logger",
        icon = R.drawable.pluto_logger___ic_logger_icon
    )

    override fun getView(): Fragment = LogsFragment()

    override fun getDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            website = "https://pluto.mocklets.com",
            vcsLink = "https://github.com/mocklets/pluto",
            developer = Developer(
                github = "srtvprateek",
                twitter = "srtv_prateek"
            )
        )
    }

    override fun onPluginInstalled(context: Context) {
    }
}
