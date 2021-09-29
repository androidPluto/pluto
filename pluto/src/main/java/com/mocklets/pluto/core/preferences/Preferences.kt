package com.mocklets.pluto.core.preferences

import android.annotation.SuppressLint
import android.content.Context

internal class Preferences(context: Context) {

    private val statePrefs by lazy { context.preferences("states") }

    private val settingsPrefs by lazy { context.preferences("settings") }

    internal var lastSessionCrash: String?
        get() = statePrefs.getString(LAST_SESSION_CRASH, null)
        @SuppressLint("ApplySharedPref")
        /* added commit, as apply() is getting missed */
        set(value) {
            statePrefs.edit().putString(LAST_SESSION_CRASH, value).commit()
        }

    internal var selectedPreferenceFiles: String?
        get() = settingsPrefs.getString(SELECTED_PREF_FILE, null)
        set(value) = settingsPrefs.edit().putString(SELECTED_PREF_FILE, value).apply()

    internal var isDarkAccessPopup: Boolean
        get() = settingsPrefs.getBoolean(IS_DARK_ACCESS_POPUP, true)
        set(value) = settingsPrefs.edit().putBoolean(IS_DARK_ACCESS_POPUP, value).apply()

    internal var isRightHandedAccessPopup: Boolean
        get() = settingsPrefs.getBoolean(IS_RIGHT_HANDED_ACCESS_POPUP, true)
        set(value) = settingsPrefs.edit().putBoolean(IS_RIGHT_HANDED_ACCESS_POPUP, value).apply()

    internal var isEasyAccessSetupDialogShown: Boolean
        get() = settingsPrefs.getBoolean(IS_EASY_ACCESS_SETUP_DIALOG_SHOWN, false)
        set(value) = settingsPrefs.edit().putBoolean(IS_EASY_ACCESS_SETUP_DIALOG_SHOWN, value).apply()

    internal var isShowIntroToast: Boolean
        get() = settingsPrefs.getBoolean(IS_SHOW_INTRO_TOAST, true)
        set(value) = settingsPrefs.edit().putBoolean(IS_SHOW_INTRO_TOAST, value).apply()

    internal companion object {
        fun isPlutoPref(it: String): Boolean {
            return it.startsWith("_pluto_pref", true)
        }

        const val DEFAULT = "Default"

        const val LAST_SESSION_CRASH = "last_session_crash"
        const val SELECTED_PREF_FILE = "selected_pref_file"
        const val IS_DARK_ACCESS_POPUP = "is_dark_access_popup"
        const val IS_RIGHT_HANDED_ACCESS_POPUP = "is_right_handed_access_popup"
        const val IS_EASY_ACCESS_SETUP_DIALOG_SHOWN = "is_easy_access_setup_dialog_shown"
        const val IS_SHOW_INTRO_TOAST = "is_show_intro_toast"
    }
}

private fun Context.preferences(name: String, mode: Int = Context.MODE_PRIVATE) = getSharedPreferences(name, mode)
