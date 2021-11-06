package com.mocklets.pluto.plugin

import androidx.annotation.DrawableRes
import java.util.*

data class PluginConfiguration(
    val name: String,
    @DrawableRes val icon: Int = R.drawable.pluto_plugin___ic_plugin_placeholder_icon,
) {
    val identifier = name.lowercase(Locale.getDefault()).replace(" ", "_", true)

    override fun equals(other: Any?): Boolean {
        return other is PluginConfiguration && identifier == other.identifier
    }

    override fun hashCode(): Int {
        return identifier.hashCode()
    }
}
