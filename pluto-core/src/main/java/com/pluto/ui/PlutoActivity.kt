package com.pluto.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.pluto.Pluto
import com.pluto.R
import com.pluto.Session
import com.pluto.applifecycle.AppState
import com.pluto.databinding.PlutoActivityPlutoBinding
import com.pluto.plugin.DeveloperDetails
import com.pluto.plugin.PluginOption
import com.pluto.plugin.PluginOptionsViewModel
import com.pluto.plugin.utilities.extensions.delayedLaunchWhenResumed
import com.pluto.plugin.utilities.sharing.ContentShare
import com.pluto.settings.OverConsentFragment
import com.pluto.settings.SettingsViewModel
import com.pluto.settings.canDrawOverlays

class PlutoActivity : AppCompatActivity() {

    private lateinit var pluginOptionsDialog: PluginOptionsDialog
    private lateinit var contentShareHelper: ContentShare
    private var pluginOptions: List<PluginOption> = emptyList()
    private var developerDetails: DeveloperDetails? = null
    private val pluginOptionsViewModel by viewModels<PluginOptionsViewModel>()
    private val settingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PlutoActivityPlutoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contentShareHelper = ContentShare(this)
        pluginOptionsDialog = PluginOptionsDialog(this) { option ->
            pluginOptionsViewModel.select(option)
        }

        Pluto.currentPlugin.removeObservers(this)
        Pluto.currentPlugin.observe(
            this,
            {
                val fragment = it.getView()
                pluginOptions = it.getOptions()
                developerDetails = it.getDeveloperDetails()
                supportFragmentManager.beginTransaction().apply {
                    this.runOnCommit {
                        it.onPluginViewCreated(it.savedInstance)
                    }
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

    override fun onResume() {
        super.onResume()
        if (!Session.isConsentAlreadyShown && Pluto.notch?.enabled == true && !canDrawOverlays()) {
            lifecycleScope.delayedLaunchWhenResumed(CONSENT_SHOW_DELAY) {
                OverConsentFragment().show(supportFragmentManager, CONSENT_SHOW_TAG)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        pluginOptionsDialog.dismiss()
    }

    private companion object {
        const val CONSENT_SHOW_DELAY = 400L
        const val CONSENT_SHOW_TAG = "overlay_consent"
    }
}
