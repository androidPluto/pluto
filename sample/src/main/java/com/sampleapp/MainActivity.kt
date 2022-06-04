package com.sampleapp

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.pluto.Pluto
import com.sampleapp.databinding.ActivityMainBinding
import com.sampleapp.plugins.SupportedPlugins
import com.sampleapp.utils.openBrowser

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
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

        binding.openPlutoCta.setOnClickListener { Pluto.open() }
        binding.showNotchCta.setOnClickListener { Pluto.showNotch(true) }
        binding.hideNotchCta.setOnClickListener { Pluto.showNotch(false) }
        binding.suggestCta.setOnClickListener { openBrowser("https://twitter.com/intent/tweet?text=@srtv_prateek+@pluto_lib") }
        binding.developCta.setOnClickListener { openBrowser("https://github.com/plutolib/pluto/wiki/Develop-Custom-Pluto-Plugins-(Beta)") }
    }

    companion object {
        const val CHIP_PADDING = 40f
    }
}
