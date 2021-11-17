package com.sampleapp

import android.content.Context
import android.preference.PreferenceManager

@Suppress("MagicNumber")
fun resetSharedPreferences(context: Context) {
    val defaultPref = PreferenceManager.getDefaultSharedPreferences(context)
    val profileDataPref = context.getSharedPreferences("profile_data", Context.MODE_PRIVATE)
    val settingsDataPref = context.getSharedPreferences("settings_data", Context.MODE_PRIVATE)

    defaultPref.edit().putInt("feed_sync_delay", 4000).apply()

    profileDataPref.edit().putString("user_name", "John Smith").apply()
    profileDataPref.edit().putString("user_email", "john.smith@gmail.com").apply()

    settingsDataPref.edit().putInt("some-int", 123).apply()
    settingsDataPref.edit().putLong("points_earned", 34L).apply()
    settingsDataPref.edit().putFloat("float", 34.45f).apply()
    settingsDataPref.edit().putBoolean("is_email_verified", true).apply()
    settingsDataPref.edit().putStringSet("preference_list", hashSetOf("science", "business")).apply()
}

fun saveAppProperties() {
//    Pluto.setAppProperties(
//        hashMapOf(
//            "user_name" to "John Smith",
//            "user_id" to "8060823b-ab8f-4f9b-bc4d-ec1acd290f23",
//            "user_location" to "Bangalore, India",
//            "user_email" to "john.smith@gmail.com",
//            "device_imei" to "49015420323751"
//        )
//    )
}
