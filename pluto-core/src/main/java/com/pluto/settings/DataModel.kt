package com.pluto.settings

import com.pluto.plugin.utilities.list.ListItem

data class SettingsEasyAccessEntity(
    val label: String = "easy_access"
) : ListItem()

data class SettingsEasyAccessPopupAppearanceEntity(
    val type: String
) : ListItem()

data class SettingsResetAllEntity(
    val type: String = "rest all"
) : ListItem()
