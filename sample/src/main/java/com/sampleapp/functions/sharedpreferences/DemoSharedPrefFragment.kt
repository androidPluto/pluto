package com.sampleapp.functions.sharedpreferences

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sampleapp.R
import com.sampleapp.databinding.FragmentDemoSharedPrefBinding

class DemoSharedPrefFragment : Fragment(R.layout.fragment_demo_shared_pref) {
    private var _binding: FragmentDemoSharedPrefBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDemoSharedPrefBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener { resetSharedPreferences(requireContext()) }
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
