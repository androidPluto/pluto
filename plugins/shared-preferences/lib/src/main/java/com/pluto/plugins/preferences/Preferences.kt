package com.pluto.plugins.preferences

import android.content.Context

internal class Preferences(context: Context) {

    private val settingsPrefs by lazy { context.preferences("settings") }

    internal var selectedPreferenceFiles: String?
        get() = settingsPrefs.getString(SELECTED_PREF_FILE, null)
        set(value) = settingsPrefs.edit().putString(SELECTED_PREF_FILE, value).apply()

    companion object {
        fun isPlutoPref(it: String): Boolean {
            return it.startsWith("_pluto_pref", true)
        }

        const val DEFAULT = "Default"
        private const val SELECTED_PREF_FILE = "selected_pref_file"
    }
}

private fun Context.preferences(name: String, mode: Int = Context.MODE_PRIVATE) = getSharedPreferences(name, mode)
