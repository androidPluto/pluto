package com.sampleapp.plugins.preferences

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.sampleapp.databinding.ActivityPreferencesBinding

class PreferencesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPreferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.close.setOnClickListener {
            finish()
        }
        binding.button.setOnClickListener {
            resetSharedPreferences(this)
        }
    }

    @SuppressWarnings("MagicNumber")
    private fun resetSharedPreferences(context: Context) {
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
}
