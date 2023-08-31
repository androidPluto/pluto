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
        this._settingsPrefs = context.preferences("_pluto_pref_lib_settings")
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

    var bandWidthLimitUploadMbps: Long
        get() = settingsPrefs.getLong(BANDWIDTH_LIMIT_UPLOAD, Long.MAX_VALUE)
        set(value) = settingsPrefs.edit().putLong(BANDWIDTH_LIMIT_UPLOAD, value).apply()

    var bandWidthLimitDownloadMbps: Long
        get() = settingsPrefs.getLong(BANDWIDTH_LIMIT_DOWNLOAD, Long.MAX_VALUE)
        set(value) = settingsPrefs.edit().putLong(BANDWIDTH_LIMIT_DOWNLOAD, value).apply()

    var bandWidthDnsResolutionDelay: Long
        get() = settingsPrefs.getLong(BANDWIDTH_LIMIT_DNS_RESOLUTION_DELAY, 0)
        set(value) = settingsPrefs.edit().putLong(BANDWIDTH_LIMIT_DNS_RESOLUTION_DELAY, value).apply()

    var isBandwidthLimitEnabled: Boolean
        get() = settingsPrefs.getBoolean(IS_BANDWIDTH_LIMIT_ENABLED, false)
        set(value) = settingsPrefs.edit().putBoolean(IS_BANDWIDTH_LIMIT_ENABLED, value).apply()

    private const val IS_DARK_THEME_ENABLED = "is_dark_theme_enabled"
    private const val IS_RIGHT_HANDED_ACCESS_POPUP = "is_right_handed_access_popup"
    private const val GRID_SIZE = "grid_size"
    private const val BANDWIDTH_LIMIT_UPLOAD = "bandwidth_limit_upload"
    private const val BANDWIDTH_LIMIT_DOWNLOAD = "bandwidth_limit_download"
    private const val BANDWIDTH_LIMIT_DNS_RESOLUTION_DELAY = "bandwidth_limit_dns_resolution_delay"
    private const val IS_BANDWIDTH_LIMIT_ENABLED = "is_bandwidth_limit_enabled"
}

private fun Context.preferences(name: String, mode: Int = Context.MODE_PRIVATE) = getSharedPreferences(name, mode)
