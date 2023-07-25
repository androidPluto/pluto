package com.pluto.plugins.layoutinspector

import androidx.fragment.app.Fragment
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginConfiguration
import com.pluto.plugins.layoutinspector.internal.ActivityLifecycle

class PlutoLayoutInspectorPlugin() : Plugin(ID) {

    @SuppressWarnings("UnusedPrivateMember")
    @Deprecated("Use the default constructor PlutoLayoutInspectorPlugin() instead.")
    constructor(identifier: String) : this()

    override fun getConfig() = PluginConfiguration(
        name = context.getString(R.string.pluto_li___plugin_name),
        icon = R.drawable.pluto_li___ic_plugin_logo,
        version = BuildConfig.VERSION_NAME
    )

    override fun getView(): Fragment = BaseFragment()

    override fun getDeveloperDetails(): DeveloperDetails {
        return DeveloperDetails(
            website = "https://androidpluto.com",
            vcsLink = "https://github.com/androidPluto/pluto",
            twitter = "https://twitter.com/android_pluto"
        )
    }

    override fun onPluginDataCleared() {
    }

    override fun onPluginInstalled() {
        application.registerActivityLifecycleCallbacks(ActivityLifecycle())
    }

    companion object {
        const val ID = "layout-inspector"
    }
}
