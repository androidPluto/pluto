package com.pluto.settings

import com.pluto.utilities.list.ListItem

/**
 * Data model representing the Easy Access setting item in the settings list.
 * This entity is used to display and manage the easy access overlay permission setting.
 *
 * @property label The identifier label for this setting, defaults to "easy_access"
 */
internal data class SettingsEasyAccessEntity(
    val label: String = "easy_access"
) : ListItem()

/**
 * Data model representing the Easy Access Popup Appearance setting item in the settings list.
 * This entity is used to display and manage the appearance of the easy access popup.
 *
 * @property type The type of appearance setting, used to determine the specific appearance option
 */
internal data class SettingsEasyAccessPopupAppearanceEntity(
    val type: String
) : ListItem()

/**
 * Data model representing the Theme setting item in the settings list.
 * This entity is used to display and manage the theme setting (light/dark).
 *
 * @property label The identifier label for this setting, defaults to "theme"
 */
internal data class SettingsThemeEntity(
    val label: String = "theme"
) : ListItem()

/**
 * Data model representing the Grid Size setting item in the settings list.
 * This entity is used to display and manage the grid size setting.
 *
 * @property label The identifier label for this setting, defaults to "grid"
 */
internal data class SettingsGridSizeEntity(
    val label: String = "grid"
) : ListItem()

/**
 * Data model representing the Reset All setting item in the settings list.
 * This entity is used to display and manage the reset all settings option.
 *
 * @property type The type identifier for this setting, defaults to "rest all"
 */
internal data class SettingsResetAllEntity(
    val type: String = "rest all"
) : ListItem()
