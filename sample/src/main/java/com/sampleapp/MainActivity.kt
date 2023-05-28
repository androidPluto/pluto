package com.sampleapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.pluto.Pluto
import com.sampleapp.ContainerFragment.Companion.FUNCTION_ID
import com.sampleapp.ContainerFragment.Companion.FUNCTION_LABEL
import com.sampleapp.databinding.ActivityMainBinding
import com.sampleapp.functions.layoutinspector.DemoLayoutInspectorActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.version.text =
            String.format(getString(R.string.version_label), BuildConfig.VERSION_NAME)
        SupportedPlugins.get().forEach {
            val chip = Chip(this).apply {
                text = it.label
                setTextAppearance(R.style.ChipTextStyle)
                textStartPadding = CHIP_PADDING
                textEndPadding = CHIP_PADDING
                setOnClickListener { _ ->
                    if (it.id == SupportedPlugins.LAYOUT_INSPECTOR) {
                        startActivity(Intent(this@MainActivity, DemoLayoutInspectorActivity::class.java))
                    } else {
                        ContainerFragment().apply {
                            arguments = Bundle().apply {
                                putString(FUNCTION_ID, it.id)
                                putString(FUNCTION_LABEL, it.label)
                            }
                            show(supportFragmentManager, "container")
                        }
                    }
                }
            }
            binding.functionGroup.addView(chip)
        }
        binding.clearLogsCta.setOnClickListener { Pluto.clearAllLogs() }
        binding.openPlutoCta.setOnClickListener { Pluto.open() }
        binding.showNotchCta.setOnClickListener {
            if (canDrawOverlays()) {
                Pluto.showNotch(true)
            } else {
                AskPermissionFragment().show(supportFragmentManager, "permission")
            }
        }
        binding.hideNotchCta.setOnClickListener {
            if (canDrawOverlays()) {
                Pluto.showNotch(false)
            } else {
                AskPermissionFragment().show(supportFragmentManager, "permission")
            }
        }
        binding.suggestCta.setOnClickListener { openBrowser("https://twitter.com/intent/tweet?text=@srtv_prateek+@android_pluto") }
        binding.developCta.setOnClickListener { openBrowser("https://github.com/androidPluto/pluto/wiki/Develop-Custom-Pluto-Plugins-(Beta)") }
    }

    companion object {
        const val CHIP_PADDING = 16f
    }
}
