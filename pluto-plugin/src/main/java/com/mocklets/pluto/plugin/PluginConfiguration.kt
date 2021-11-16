package com.mocklets.pluto.plugin

import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment

data class PluginConfiguration(
    val name: String,
    @DrawableRes val icon: Int = R.drawable.pluto___ic_plugin_placeholder_icon,
    val fragment: Fragment
) {
    val identifier = name.lowercase().replace(" ", "_", true)

    override fun equals(other: Any?): Boolean {
        return other is PluginConfiguration && identifier == other.identifier
    }

    override fun hashCode(): Int {
        return identifier.hashCode()
    }
}
