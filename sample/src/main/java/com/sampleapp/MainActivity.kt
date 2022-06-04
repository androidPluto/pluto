package com.sampleapp

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.sampleapp.databinding.ActivityMainBinding
import com.sampleapp.plugins.SupportedPlugins

class MainActivity : AppCompatActivity() {

//    private val pluginAdapter: ListAdapter by lazy { PluginListAdapter(onActionListener) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
//        val javaTest = JavaTest()
        setContentView(binding.root)

        binding.version.text = String.format(getString(R.string.version_label), BuildConfig.VERSION_NAME)
        SupportedPlugins.get().forEach {
            val chip = Chip(this).apply {
                text = it.title.capitalize()
                setTextAppearance(R.style.ChipTextStyle)
                textStartPadding = CHIP_PADDING
                textEndPadding = CHIP_PADDING
                setOnClickListener { _ ->
                    Toast.makeText(this@MainActivity, it.title, LENGTH_SHORT).show()
                }
            }
            binding.functionGroup.addView(chip)
        }

//        binding.crashList.apply {
//            adapter = pluginAdapter
//        }
//        pluginAdapter.list = SupportedPlugins.get()

//        binding.showNotch.setOnClickListener {
//            if (IS_TESTING_JAVA) {
//                javaTest.showNotch(true)
//            } else {
//                Pluto.showNotch(true)
//            }
//        }
//
//        binding.hideNotch.setOnClickListener {
//            if (IS_TESTING_JAVA) {
//                javaTest.showNotch(false)
//            } else {
//                Pluto.showNotch(false)
//            }
//        }
//
//        binding.open.setOnClickListener {
//            if (IS_TESTING_JAVA) {
//                javaTest.open()
//            } else {
//                Pluto.open()
//            }
//        }
//
//        binding.openDemoPlugin.setOnClickListener {
//            if (IS_TESTING_JAVA) {
//                javaTest.open(DEMO_PLUGIN_ID)
//            } else {
//                Pluto.open(DEMO_PLUGIN_ID)
//            }
//        }
    }

//    private val onActionListener = object : DiffAdapter.OnActionListener {
//        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
//            if (data is PluginListItem) {
//                SupportedPlugins.openPlugin(this@MainActivity, data)
//            }
//        }
//    }
//
    companion object {
        const val CHIP_PADDING = 40f
//        const val IS_TESTING_JAVA = true
    }
}
