package com.pluto.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pluto.Pluto
import com.pluto.R
import com.pluto.applifecycle.AppState
import com.pluto.databinding.PlutoActivityPlutoBaseBinding
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.PluginOption
import com.pluto.plugin.PluginOptionsViewModel
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.sharing.ContentShare
import com.pluto.settings.SettingsViewModel

class PlutoBaseActivity : AppCompatActivity() {

    private lateinit var pluginOptionsDialog: PluginOptionsDialog
    private lateinit var contentShareHelper: ContentShare
    private var pluginOptions: List<PluginOption> = emptyList()
    private var developerDetails: DeveloperDetails? = null
    private val pluginOptionsViewModel by viewModels<PluginOptionsViewModel>()
    private val settingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PlutoActivityPlutoBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contentShareHelper = ContentShare(this)
        pluginOptionsDialog = PluginOptionsDialog(this) { option ->
            pluginOptionsViewModel.select(option)
        }

        binding.close.setDebounceClickListener {
            finish()
        }

        binding.options.setDebounceClickListener {
            pluginOptionsDialog.show(pluginOptions, developerDetails)
        }

        Pluto.currentPlugin.removeObservers(this)
        Pluto.currentPlugin.observe(
            this,
            {
                binding.title.text = it.getConfig().name
                val fragment = it.getView()
                pluginOptions = it.getOptions()
                developerDetails = it.getDeveloperDetails()
                supportFragmentManager.beginTransaction().apply {
                    this.runOnCommit { it.onPluginViewCreated(it.savedInstance) }
                    this.replace(R.id.container, fragment).commit()
                }
            }
        )

        Pluto.appState.observe(
            this,
            {
                if (it is AppState.Background) {
                    finish()
                }
            }
        )

        settingsViewModel.resetAll.observe(
            this,
            {
                Pluto.pluginManager.installedPlugins.forEach {
                    it.onPluginDataCleared()
                }
            }
        )
    }
}
