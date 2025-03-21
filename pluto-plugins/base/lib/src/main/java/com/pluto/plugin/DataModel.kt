package com.pluto.plugin

import androidx.annotation.DrawableRes

/**
 * Data class containing details about a plugin's developer.
 *
 * This class is used to provide information about the developer of a plugin,
 * which can be displayed in the plugin's details screen.
 *
 * @property vcsLink Optional link to the version control system repository
 * @property website Optional link to the developer's website
 * @property twitter Optional link to the developer's Twitter profile
 */
data class DeveloperDetails(
    val vcsLink: String? = null,
    val website: String? = null,
    val twitter: String? = null
)

/**
 * Data class containing configuration for a plugin.
 *
 * This class defines the visual and metadata properties of a plugin,
 * such as its name, icon, and version.
 *
 * @property name The display name of the plugin
 * @property icon The resource ID of the plugin's icon
 * @property version The version string of the plugin
 */
data class PluginConfiguration(
    val name: String,
    @DrawableRes val icon: Int = R.drawable.pluto___ic_plugin_placeholder_icon,
    val version: String
)

/**
 * Data class containing configuration for a plugin group.
 *
 * This class defines the visual properties of a plugin group,
 * such as its name and icon.
 *
 * @property name The display name of the plugin group
 * @property icon The resource ID of the plugin group's icon
 */
data class PluginGroupConfiguration(
    val name: String,
    @DrawableRes val icon: Int = R.drawable.pluto___ic_plugin_group_placeholder_icon,
)
