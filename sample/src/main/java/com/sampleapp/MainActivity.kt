package com.sampleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluto.Pluto
import com.sampleapp.SampleApp.Companion.DEMO_PLUGIN_ID
import com.sampleapp.databinding.ActivityMainBinding

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
    }

    companion object {
        const val IS_TESTING_JAVA = true
    }
}
