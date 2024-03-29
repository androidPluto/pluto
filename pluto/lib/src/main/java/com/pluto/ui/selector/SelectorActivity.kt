package com.pluto.ui.selector

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.annotation.AnimRes
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.pluto.Pluto
import com.pluto.R
import com.pluto.core.applifecycle.AppStateCallback
import com.pluto.databinding.PlutoActivityPluginSelectorBinding
import com.pluto.maven.MavenSession
import com.pluto.maven.MavenViewModel
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginEntity
import com.pluto.plugin.PluginGroup
import com.pluto.plugin.PluginsViewModel
import com.pluto.plugin.selector.PluginAdapter
import com.pluto.settings.SettingsFragment
import com.pluto.settings.SettingsViewModel
import com.pluto.tool.PlutoTool
import com.pluto.tool.ToolsViewModel
import com.pluto.tool.selector.ToolAdapter
import com.pluto.ui.ListWrapper
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
    private val mavenViewModel by viewModels<MavenViewModel>()
    private lateinit var binding: PlutoActivityPluginSelectorBinding
    private lateinit var selectorUtils: SelectorUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlutoActivityPluginSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(R.anim.pluto___slide_in_bottom, R.anim.pluto___slide_out_bottom)
        selectorUtils = SelectorUtils(this)

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
        binding.mavenVersion.setOnDebounceClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(MavenSession.releaseUrl)))
        }

        Pluto.appStateCallback.state.removeObserver(appStateListener)
        Pluto.appStateCallback.state.observe(this, appStateListener)

        pluginsViewModel.plugins.removeObserver(pluginListObserver)
        pluginsViewModel.plugins.observe(this, pluginListObserver)
        toolsViewModel.tools.removeObserver(toolListObserver)
        toolsViewModel.tools.observe(this, toolListObserver)
        mavenViewModel.latestVersion.removeObserver(mavenVersionObserver)
        mavenViewModel.latestVersion.observe(this, mavenVersionObserver)

        settingsViewModel.resetAll.observe(this) {
            Pluto.pluginManager.installedPlugins.forEach {
                when (it) {
                    is Plugin -> it.onPluginDataCleared()
                    is PluginGroup -> it.installedPlugins.forEach { plugin -> plugin.onPluginDataCleared() }
                }
            }
            Pluto.resetDataCallback.state.postValue(true)
        }

        mavenViewModel.getLatestVersion()
    }

    private val pluginListObserver = Observer<List<PluginEntity>> {
        pluginAdapter.list = arrayListOf<ListWrapper<PluginEntity>>().apply {
            it.forEach { entity ->
                add(ListWrapper(entity))
            }
        }
        if (it.isNullOrEmpty()) {
            binding.noPluginView.visibility = if (it.isNullOrEmpty()) VISIBLE else GONE
        }
    }

    private val toolListObserver = Observer<List<PlutoTool>> {
        toolAdapter.list = it
    }

    private val mavenVersionObserver = Observer<String> {
        binding.mavenVersion.isVisible = !it.isNullOrEmpty()
        binding.mavenVersion.text = String.format(getString(R.string.pluto___new_version_available_text), it)
    }

    private val appStateListener = Observer<AppStateCallback.State> {
        if (it is AppStateCallback.State.Background) {
            finish()
        }
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            selectorUtils.onSelect(action, data)
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
