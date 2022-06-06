package com.sampleapp.functions.datastore

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sampleapp.R
import com.sampleapp.databinding.FragmentDemoDatastorePrefBinding
import com.sampleapp.functions.datastore.DemoDatastorePrefFragment.Companion.APP_STATE_PREF_NAME
import com.sampleapp.functions.datastore.DemoDatastorePrefFragment.Companion.USER_STATE_PREF_NAME
import kotlinx.coroutines.launch

class DemoDatastorePrefFragment : Fragment(R.layout.fragment_demo_datastore_pref) {
    private var _binding: FragmentDemoDatastorePrefBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDemoDatastorePrefBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener { initDataForDataStoreSample(requireContext()) }
        initDataForDataStoreSample(requireContext())
    }

    private fun initDataForDataStoreSample(context: Context) {
        lifecycleScope.launch {
            context.userStateDatastore.edit {
                it[booleanPreferencesKey("is_logged_in")] = true
                it[stringPreferencesKey("auth_token")] = "asljknva38uv972gv"
                it[stringPreferencesKey("refresh_token")] = "iuch21d2c1acbkufh2918hcb1837bc1a"
            }
            context.appStateDatastore.edit {
                it[booleanPreferencesKey("is_latest_version")] = false
                it[stringPreferencesKey("session_uuid")] = "9522b353-e3a9-428c-9af6-338fd5e9f9d6"
                it[longPreferencesKey("session_duration")] = RANDOM_LONG
                it[floatPreferencesKey("pi_value")] = PI_VALUE
            }
        }
    }

    companion object {
        const val RANDOM_LONG = 13_101_993L
        const val PI_VALUE = 3.141592653589793238462643383279502884197f
        const val APP_STATE_PREF_NAME = "app states"
        const val USER_STATE_PREF_NAME = "user states"
    }
}

val Context.appStateDatastore by preferencesDataStore(name = APP_STATE_PREF_NAME)
val Context.userStateDatastore by preferencesDataStore(name = USER_STATE_PREF_NAME)
