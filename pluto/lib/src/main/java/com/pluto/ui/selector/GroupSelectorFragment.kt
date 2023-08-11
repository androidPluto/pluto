package com.pluto.ui.selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.Pluto
import com.pluto.R
import com.pluto.databinding.PlutoFragmentGroupSelectorBinding
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginGroup
import com.pluto.plugin.selector.PluginGroupAdapter
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.viewBinding

internal class GroupSelectorFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoFragmentGroupSelectorBinding::bind)
    private val pluginsGroupViewModel by activityViewModels<PluginsGroupViewModel>()
    private val pluginAdapter: BaseAdapter by lazy { PluginGroupAdapter(onActionListener) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto___fragment_group_selector, container, false)

    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.apply {
            adapter = pluginAdapter
            layoutManager = LinearLayoutManager(context)
        }

        pluginsGroupViewModel.current.removeObserver(pluginGroupObserver)
        pluginsGroupViewModel.current.observe(this, pluginGroupObserver)
    }

    private val pluginGroupObserver = Observer<PluginGroup> {
        binding.title.text = it.getConfig().name
        pluginAdapter.list = it.installedPlugins
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            when (data) {
                is Plugin -> when (action) {
                    "click" -> {
                        Pluto.open(data.identifier)
                        activity?.finish()
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
                        devDetailsFragment.show(childFragmentManager, "devDetails")
                    }
                }
            }
        }
    }
}
