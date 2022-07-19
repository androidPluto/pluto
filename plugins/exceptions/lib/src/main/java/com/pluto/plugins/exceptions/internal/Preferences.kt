package com.pluto.plugins.exceptions.internal

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

@SuppressWarnings("UseDataClass")
internal class Preferences(context: Context) {

    private val statePrefs: SharedPreferences = context.getSharedPreferences("_pluto_exception", Context.MODE_PRIVATE)

    internal var lastSessionCrash: String?
        get() = statePrefs.getString(LAST_SESSION_CRASH, null)
        @SuppressLint("ApplySharedPref")
        /* added commit, as apply() is getting missed on crash */
        set(value) {
            statePrefs.edit().putString(LAST_SESSION_CRASH, value).commit()
        }

    private companion object {
        const val LAST_SESSION_CRASH = "last_session_crash"
    }
}
