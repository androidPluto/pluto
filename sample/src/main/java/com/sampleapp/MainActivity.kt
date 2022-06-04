package com.sampleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluto.Pluto
import com.sampleapp.databinding.ActivityMainBinding
import com.sampleapp.list.PluginListAdapter
import com.sampleapp.list.PluginListItem
import com.sampleapp.plugins.SupportedPlugins
import com.sampleapp.utils.DiffAdapter
import com.sampleapp.utils.DiffAwareHolder
import com.sampleapp.utils.ListAdapter
import com.sampleapp.utils.ListItem

class MainActivity : AppCompatActivity() {

    private val pluginAdapter: ListAdapter by lazy { PluginListAdapter(onActionListener) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val javaTest = JavaTest()
        setContentView(binding.root)

        binding.crashList.apply {
            adapter = pluginAdapter
        }
        pluginAdapter.list = SupportedPlugins.get()
        binding.version.text = String.format(getString(R.string.version_label), BuildConfig.VERSION_NAME)

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
        binding.open.setOnClickListener {
            if (IS_TESTING_JAVA) {
                javaTest.open()
            } else {
                Pluto.open()
            }
        }
//
//        binding.openDemoPlugin.setOnClickListener {
//            if (IS_TESTING_JAVA) {
//                javaTest.open(DEMO_PLUGIN_ID)
//            } else {
//                Pluto.open(DEMO_PLUGIN_ID)
//            }
//        }
    }

    private val onActionListener = object : DiffAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is PluginListItem) {
                SupportedPlugins.openPlugin(this@MainActivity, data)
            }
        }
    }

    companion object {
        const val IS_TESTING_JAVA = true
    }
}
