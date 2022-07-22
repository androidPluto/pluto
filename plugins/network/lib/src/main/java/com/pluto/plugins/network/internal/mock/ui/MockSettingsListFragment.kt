package com.pluto.plugins.network.internal.mock.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.utilities.extensions.dp
import com.pluto.plugin.utilities.extensions.hideKeyboard
import com.pluto.plugin.utilities.extensions.onBackPressed
import com.pluto.plugin.utilities.extensions.showKeyboard
import com.pluto.plugin.utilities.list.BaseAdapter
import com.pluto.plugin.utilities.list.CustomItemDecorator
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkFragmentMockSettingsListBinding
import com.pluto.plugins.network.internal.mock.logic.MockSettingsViewModel
import com.pluto.plugins.network.internal.mock.logic.dao.MockSettingsEntity
import com.pluto.plugins.network.internal.mock.ui.list.MockSettingsItemAdapter

internal class MockSettingsListFragment : Fragment(R.layout.pluto_network___fragment_mock_settings_list) {

    private val binding by viewBinding(PlutoNetworkFragmentMockSettingsListBinding::bind)
    private val viewModel: MockSettingsViewModel by activityViewModels()
    private lateinit var mockSettingsAdapter: BaseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { handleBackPress() }
        mockSettingsAdapter = MockSettingsItemAdapter(onActionListener)
        binding.apiList.apply {
            adapter = mockSettingsAdapter
            addItemDecoration(CustomItemDecorator(context, DECORATOR_DIVIDER_PADDING))
        }
        binding.close.setOnDebounceClickListener {
            handleBackPress()
        }
        binding.search.setOnDebounceClickListener {
            binding.searchView.visibility = View.VISIBLE
            binding.searchView.requestFocus()
        }
        binding.closeSearch.setOnDebounceClickListener {
            exitSearch()
        }
        binding.clearSearch.setOnDebounceClickListener {
            binding.editSearch.text = null
        }
        binding.editSearch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.showKeyboard()
            } else {
                v.hideKeyboard()
            }
        }
        binding.editSearch.doOnTextChanged { text, _, _, _ ->
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                text?.toString()?.let {
                    viewModel.fetchList(it)
                }
            }
        }

        viewModel.mockList.removeObserver(networkProxyObserver)
        viewModel.mockList.observe(viewLifecycleOwner, networkProxyObserver)
        viewModel.fetchList()
    }

    private val networkProxyObserver = Observer<List<MockSettingsEntity>> {
        mockSettingsAdapter.list = it
        binding.noItemText.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun exitSearch() {
        binding.editSearch.text = null
        binding.searchView.visibility = View.GONE
        binding.editSearch.clearFocus()
    }

    private fun handleBackPress() {
        if (binding.searchView.isVisible) {
            exitSearch()
        } else {
            findNavController().navigateUp()
        }
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            when (data) {
                is MockSettingsEntity -> {
                    val bundle = bundleOf("url" to data.requestUrl, "method" to data.requestMethod)
                    findNavController().navigate(R.id.openMockSettingsEdit, bundle)
                }
            }
        }
    }

    private companion object {
        val DECORATOR_DIVIDER_PADDING = 16f.dp.toInt()
    }
}
