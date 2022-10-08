package com.pluto.plugin

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pluto.Pluto
import com.pluto.R
import com.pluto.applifecycle.AppState
import com.pluto.databinding.PlutoActivityPluginSelectorBinding
import com.pluto.plugin.list.PluginAdapter
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.settings.SettingsFragment
import com.pluto.settings.SettingsViewModel

class PluginSelectorActivity : FragmentActivity() {

    private val pluginsViewModel by viewModels<PluginsViewModel>()
    private val pluginAdapter: BaseAdapter by lazy { PluginAdapter(onActionListener) }
    private val settingsViewModel by viewModels<SettingsViewModel>()
    private lateinit var binding: PlutoActivityPluginSelectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlutoActivityPluginSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(R.anim.pluto___slide_in_bottom, R.anim.pluto___slide_out_bottom)

        binding.list.apply {
            adapter = pluginAdapter
            layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT)
        }

        binding.toolsList.apply {
            adapter = pluginAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

        binding.root.setOnDebounceClickListener {
            finish()
        }

        binding.version.setSpan {
            append(fontColor(light("v"), context.color(R.color.pluto___white_40)))
            append(regular(com.pluto.BuildConfig.VERSION_NAME))
        }

        binding.settings.setOnDebounceClickListener {
            SettingsFragment().show(supportFragmentManager, "settings")
        }

        Pluto.appState.removeObserver(appStateListener)
        Pluto.appState.observe(this, appStateListener)

        pluginsViewModel.plugins.removeObserver(pluginListObserver)
        pluginsViewModel.plugins.observe(this, pluginListObserver)

        settingsViewModel.resetAll.observe(this) {
            Pluto.pluginManager.installedPlugins.forEach {
                it.onPluginDataCleared()
            }
            Pluto.close()
        }
    }

    private val pluginListObserver = Observer<List<Plugin>> {
        pluginAdapter.list = it
        if (it.isNullOrEmpty()) {
            binding.noPluginView.visibility = if (it.isNullOrEmpty()) VISIBLE else GONE
        }
    }

    private val appStateListener = Observer<AppState> {
        if (it is AppState.Background) {
            finish()
        }
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is Plugin) {
                when (action) {
                    "click" -> {
                        Pluto.open(data.devIdentifier)
                        finish()
                    }
                    "long_click" -> {
                        val devDetailsFragment = DevDetailsFragment()
                        devDetailsFragment.arguments = Bundle().apply {
                            putString("name", data.getConfig().name)
                            putInt("icon", data.getConfig().icon)
                            putString("version", data.getConfig().version)
                            putString("website", data.getDeveloperDetails()?.website)
                            putString("vcs", data.getDeveloperDetails()?.vcsLink)
                            putString("twitter", data.getDeveloperDetails()?.twitter)
                        }
                        devDetailsFragment.show(supportFragmentManager, "devDetails")
                    }
                }
            }
        }
    }

    private companion object {
        const val GRID_SPAN_COUNT = 4
    }
}
