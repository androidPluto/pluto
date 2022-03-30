package com.sampleapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.pluto.Pluto
import com.pluto.plugin.datastore.pref.PlutoDataStoreWatcher
import com.sampleapp.SampleApp.Companion.DEMO_PLUGIN_ID
import com.sampleapp.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val javaTest = JavaTest()
        setContentView(binding.root)

        binding.showNotch.setOnClickListener {
            if (IS_TESTING_JAVA) {
                javaTest.showNotch(true)
            } else {
                Pluto.showNotch(true)
            }
        }

        binding.hideNotch.setOnClickListener {
            if (IS_TESTING_JAVA) {
                javaTest.showNotch(false)
            } else {
                Pluto.showNotch(false)
            }
        }

        binding.openSelector.setOnClickListener {
            if (IS_TESTING_JAVA) {
                javaTest.open()
            } else {
                Pluto.open()
            }
        }

        binding.openDemoPlugin.setOnClickListener {
            if (IS_TESTING_JAVA) {
                javaTest.open(DEMO_PLUGIN_ID)
            } else {
                Pluto.open(DEMO_PLUGIN_ID)
            }
        }

        initDataForDataStoreSample()
    }

    private fun initDataForDataStoreSample() {
        PlutoDataStoreWatcher.watch("prefrence name", dataStore)
        PlutoDataStoreWatcher.watch("user_info", dataStore2)
        lifecycleScope.launch {
            dataStore2.edit {
                it[booleanPreferencesKey("isLoggedIn")] = true
                it[stringPreferencesKey("auth_token")] = "asljknva38uv972gv"
                it[stringPreferencesKey("refresh_token")] = "iuch21d2c1acbkufh2918hcb1837bc1a"
            }
            dataStore.edit {
                it[booleanPreferencesKey("random_boolean")] = false
                it[stringPreferencesKey("random_string")] = "random string value"
                it[longPreferencesKey("random_long")] = 13101993
                it[floatPreferencesKey("random_float")] = 3.141592653589793238462643383279502884197f
            }
        }
    }

    companion object {
        const val IS_TESTING_JAVA = true
    }
}


private val Context.dataStore by preferencesDataStore(
    name = "prefrence name"
)
private val Context.dataStore2 by preferencesDataStore(
    name = "user_info"
)
