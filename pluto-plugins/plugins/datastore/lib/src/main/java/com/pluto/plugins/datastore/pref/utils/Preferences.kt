package com.pluto.plugins.datastore.pref.utils

import android.content.Context

internal class Preferences(context: Context) {

    private val settingsPrefs by lazy { context.preferences("_pluto_datastore_pref_settings") }

    internal var selectedPreferenceFiles: String?
        get() = settingsPrefs.getString(SELECTED_PREF_FILE, null)
        set(value) = settingsPrefs.edit().putString(SELECTED_PREF_FILE, value).apply()

    companion object {
        private const val SELECTED_PREF_FILE = "selected_datastore_pref_file"
    }
}

private fun Context.preferences(name: String, mode: Int = Context.MODE_PRIVATE) = getSharedPreferences(name, mode)
