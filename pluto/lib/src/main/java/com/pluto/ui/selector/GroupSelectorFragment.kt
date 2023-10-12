package com.pluto.ui.selector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pluto.R
import com.pluto.databinding.PlutoFragmentGroupSelectorBinding
import com.pluto.plugin.Plugin
import com.pluto.plugin.PluginGroup
import com.pluto.plugin.selector.PluginGroupAdapter
import com.pluto.ui.ListWrapper
import com.pluto.utilities.extensions.dp
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.CustomItemDecorator
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.viewBinding

internal class GroupSelectorFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(PlutoFragmentGroupSelectorBinding::bind)
    private val pluginsGroupViewModel by activityViewModels<PluginsGroupViewModel>()
    private val pluginAdapter: BaseAdapter by lazy { PluginGroupAdapter(onActionListener) }
    private lateinit var selectorUtils: SelectorUtils

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.pluto___fragment_group_selector, container, false)

    override fun getTheme(): Int = R.style.PlutoBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectorUtils = SelectorUtils(this)

        binding.list.apply {
            adapter = pluginAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(CustomItemDecorator(context, DECORATOR_DIVIDER_PADDING))
        }

        pluginsGroupViewModel.current.removeObserver(pluginGroupObserver)
        pluginsGroupViewModel.current.observe(this, pluginGroupObserver)
    }

    private val pluginGroupObserver = Observer<PluginGroup> {
        binding.title.text = it.getConfig().name
        pluginAdapter.list = arrayListOf<ListWrapper<Plugin>>().apply {
            it.installedPlugins.forEach { plugin ->
                add(ListWrapper(plugin))
            }
        }
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            selectorUtils.onSelect(action, data)
        }
    }

    private companion object {
        val DECORATOR_DIVIDER_PADDING = 16f.dp.toInt()
    }
}
