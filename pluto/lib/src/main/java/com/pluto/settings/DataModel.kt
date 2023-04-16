package com.pluto.settings

import com.pluto.utilities.list.ListItem

internal data class SettingsEasyAccessEntity(
    val label: String = "easy_access"
) : ListItem()

internal data class SettingsEasyAccessPopupAppearanceEntity(
    val type: String
) : ListItem()

internal data class SettingsThemeEntity(
    val label: String = "theme"
) : ListItem()

internal data class SettingsGridSizeEntity(
    val label: String = "grid"
) : ListItem()

internal data class SettingsResetAllEntity(
    val type: String = "rest all"
) : ListItem()
