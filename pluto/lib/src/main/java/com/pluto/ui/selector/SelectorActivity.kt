package com.pluto.ui.selector

import android.content.Context
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.annotation.AnimRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.pluto.Pluto
import com.pluto.R
import com.pluto.databinding.PlutoActivityPluginSelectorBinding
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginsViewModel
import com.pluto.plugin.selector.PluginAdapter
import com.pluto.settings.SettingsFragment
import com.pluto.settings.SettingsViewModel
import com.pluto.tool.PlutoTool
import com.pluto.tool.ToolsViewModel
import com.pluto.tool.selector.ToolAdapter
import com.pluto.utilities.AppState
import com.pluto.utilities.extensions.canDrawOverlays
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.openOverlaySettings
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan

class SelectorActivity : FragmentActivity() {

    private val pluginsViewModel by viewModels<PluginsViewModel>()
    private val pluginAdapter: BaseAdapter by lazy { PluginAdapter(onActionListener) }
    private val toolsViewModel by viewModels<ToolsViewModel>()
    private val toolAdapter: BaseAdapter by lazy { ToolAdapter(onActionListener) }
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
            adapter = toolAdapter
            layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT)
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

        binding.overlaySetting.visibility = if (canDrawOverlays()) GONE else VISIBLE
        binding.overlaySetting.setOnDebounceClickListener {
            openOverlaySettings()
        }

        Pluto.appStateCallback.state.removeObserver(appStateListener)
        Pluto.appStateCallback.state.observe(this, appStateListener)

        pluginsViewModel.plugins.removeObserver(pluginListObserver)
        pluginsViewModel.plugins.observe(this, pluginListObserver)
        toolsViewModel.tools.removeObserver(toolListObserver)
        toolsViewModel.tools.observe(this, toolListObserver)

        settingsViewModel.resetAll.observe(this) {
            Pluto.pluginManager.installedPlugins.forEach {
                it.onPluginDataCleared()
            }
            Pluto.resetDataCallback.state.postValue(true)
        }
    }

    private val pluginListObserver = Observer<List<Plugin>> {
        pluginAdapter.list = it
        if (it.isNullOrEmpty()) {
            binding.noPluginView.visibility = if (it.isNullOrEmpty()) VISIBLE else GONE
        }
    }

    private val toolListObserver = Observer<List<PlutoTool>> {
        toolAdapter.list = it
    }

    private val appStateListener = Observer<AppState> {
        if (it is AppState.Background) {
            finish()
        }
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            when (data) {
                is Plugin -> when (action) {
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
                is PlutoTool -> {
                    Pluto.toolManager.select(data.id)
                    finish()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Pluto.selectorStateCallback.state.postValue(true)
    }

    override fun onStop() {
        super.onStop()
        Pluto.selectorStateCallback.state.postValue(false)
    }

    companion object {
        private const val GRID_SPAN_COUNT = 4
        const val ANIMATION_DURATION = 250L
    }
}

fun Context.loadAnimation(@AnimRes id: Int): Animation {
    return AnimationUtils.loadAnimation(this, id)
}
