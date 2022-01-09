package com.pluto.settings

import com.pluto.plugin.utilities.list.ListItem

internal data class SettingsEasyAccessEntity(
    val label: String = "easy_access"
) : ListItem()

internal data class SettingsEasyAccessPopupAppearanceEntity(
    val type: String
) : ListItem()

internal data class SettingsResetAllEntity(
    val type: String = "rest all"
) : ListItem()
