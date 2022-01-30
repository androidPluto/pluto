package com.pluto.plugin

data class UiBridgeComponents(
    val activityClass: Class<*>,
    val idLabel: String = "pluginIdentifier",
    val bundleLabel: String = "pluginBundle"
)
