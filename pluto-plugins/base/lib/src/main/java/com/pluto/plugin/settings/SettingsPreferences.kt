package com.pluto.plugin.settings

import android.content.Context
import android.content.SharedPreferences

object SettingsPreferences {

    private val settingsPrefs: SharedPreferences
        get() = returnSettings()
    private var _settingsPrefs: SharedPreferences? = null
    private fun returnSettings(): SharedPreferences {
        _settingsPrefs?.let { return it }
        throw IllegalStateException("Settings preferences is not initialised yet.")
    }

    fun init(context: Context) {
        _settingsPrefs = context.preferences("_pluto_pref_lib_settings")
    }

    var isDarkThemeEnabled: Boolean
        get() = settingsPrefs.getBoolean(IS_DARK_THEME_ENABLED, true)
        set(value) = settingsPrefs.edit().putBoolean(IS_DARK_THEME_ENABLED, value).apply()

    var isRightHandedAccessPopup: Boolean
        get() = settingsPrefs.getBoolean(IS_RIGHT_HANDED_ACCESS_POPUP, true)
        set(value) = settingsPrefs.edit().putBoolean(IS_RIGHT_HANDED_ACCESS_POPUP, value).apply()

    var gridSize: Int
        get() = settingsPrefs.getInt(GRID_SIZE, 5)
        set(value) = settingsPrefs.edit().putInt(GRID_SIZE, value).apply()

    private const val IS_DARK_THEME_ENABLED = "is_dark_theme_enabled"
    private const val IS_RIGHT_HANDED_ACCESS_POPUP = "is_right_handed_access_popup"
    private const val GRID_SIZE = "grid_size"
}

private fun Context.preferences(name: String, mode: Int = Context.MODE_PRIVATE) = getSharedPreferences(name, mode)
