package com.pluto.plugin

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

data class DeveloperDetails(
    val vcsLink: String? = null,
    val website: String? = null,
    val twitter: String? = null
)

data class PluginConfiguration(
    val name: String,
    @DrawableRes val icon: Int = R.drawable.pluto___ic_plugin_placeholder_icon,
    val version: String,
    @ColorInt val assentColorInt: Int = Color.parseColor("#ff0000")
)

data class PluginGroupConfiguration(
    val name: String,
    @DrawableRes val icon: Int = R.drawable.pluto___ic_plugin_group_placeholder_icon,
)
