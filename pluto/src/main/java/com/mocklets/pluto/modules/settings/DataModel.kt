package com.mocklets.pluto.modules.settings

import com.mocklets.pluto.core.ui.list.ListItem

data class SettingsEasyAccessEntity(
    val label: String = "easy_access"
) : ListItem()

data class SettingsClearDataEntity(
    val id: String,
    val label: String
) : ListItem()

data class SettingsSharedPrefEntity(
    val label: String = "shared_pref"
) : ListItem()

data class SettingsLinkMockletsEntity(
    val label: String = "mocklets"
) : ListItem()

data class SettingsEasyAccessPopupAppearanceEntity(
    val type: String
) : ListItem()

data class SettingsResetAllEntity(
    val type: String = "rest all"
) : ListItem()
