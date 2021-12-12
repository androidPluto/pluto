package com.mocklets.pluto.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mocklets.pluto.R
import com.mocklets.pluto.databinding.PlutoFragmentPluginSelectorBinding
import com.mocklets.pluto.plugin.Plugin
import com.mocklets.pluto.plugin.PluginsViewModel
import com.mocklets.pluto.utilities.DebugLog
import com.mocklets.pluto.utilities.list.BaseAdapter
import com.mocklets.pluto.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.utilities.list.DiffAwareHolder
import com.mocklets.pluto.utilities.list.ListItem
import com.mocklets.pluto.utilities.viewBinding

class PluginSelectorDialogFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoFragmentPluginSelectorBinding::bind)
    private val pluginsViewModel by activityViewModels<PluginsViewModel>()
    private val pluginAdapter: BaseAdapter by lazy { PluginAdapter(onActionListener) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto___fragment_plugin_selector, container, false)

    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.apply {
            adapter = pluginAdapter
        }

        pluginsViewModel.plugins.removeObserver(pluginListObserver)
        pluginsViewModel.plugins.observe(viewLifecycleOwner, pluginListObserver)
    }

    private val pluginListObserver = Observer<List<Plugin>> {
        DebugLog.e("Prateek", it.size.toString())
        pluginAdapter.list = it
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
//            if (data is Plugin) {
//
//            }
        }
    }
}
