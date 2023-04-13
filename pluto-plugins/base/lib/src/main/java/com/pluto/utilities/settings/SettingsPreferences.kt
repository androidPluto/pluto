package com.pluto.utilities.settings

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
        this._settingsPrefs = context.preferences("settings")
    }

    var isDarkAccessPopup: Boolean
        get() = settingsPrefs.getBoolean(IS_DARK_ACCESS_POPUP, true)
        set(value) = settingsPrefs.edit().putBoolean(IS_DARK_ACCESS_POPUP, value).apply()

    var isRightHandedAccessPopup: Boolean
        get() = settingsPrefs.getBoolean(IS_RIGHT_HANDED_ACCESS_POPUP, true)
        set(value) = settingsPrefs.edit().putBoolean(IS_RIGHT_HANDED_ACCESS_POPUP, value).apply()

    private const val IS_DARK_ACCESS_POPUP = "is_dark_access_popup"
    private const val IS_RIGHT_HANDED_ACCESS_POPUP = "is_right_handed_access_popup"
}

private fun Context.preferences(name: String, mode: Int = Context.MODE_PRIVATE) = getSharedPreferences(name, mode)
