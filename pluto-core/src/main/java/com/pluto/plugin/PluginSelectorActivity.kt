package com.pluto.plugin

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
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
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.settings.SettingsFragment

class PluginSelectorActivity : AppCompatActivity() {

    private val pluginsViewModel by viewModels<PluginsViewModel>()
    private val pluginAdapter: BaseAdapter by lazy { PluginAdapter(onActionListener) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = PlutoActivityPluginSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.list.apply {
            adapter = pluginAdapter
            layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT)
        }

        binding.root.setDebounceClickListener {
            finish()
        }

        binding.version.setSpan {
            append(fontColor(light("v"), context.color(R.color.pluto___white_40)))
            append(regular(com.pluto.BuildConfig.VERSION_NAME))
        }

        binding.settings.setDebounceClickListener {
            SettingsFragment().show(supportFragmentManager, "settings")
        }

        Pluto.appState.removeObserver(appStateListener)
        Pluto.appState.observe(this, appStateListener)

        pluginsViewModel.plugins.removeObserver(pluginListObserver)
        pluginsViewModel.plugins.observe(this, pluginListObserver)
    }

    private val pluginListObserver = Observer<List<Plugin>> {
        pluginAdapter.list = it
    }

    private val appStateListener = Observer<AppState> {
        if (it is AppState.Background) {
            finish()
        }
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is Plugin) {
                Pluto.open(data.getConfig().identifier)
                finish()
            }
        }
    }

    private companion object {
        const val GRID_SPAN_COUNT = 4
    }
}
