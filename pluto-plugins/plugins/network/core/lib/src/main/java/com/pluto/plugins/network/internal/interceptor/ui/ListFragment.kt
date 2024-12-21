package com.pluto.plugins.network.internal.interceptor.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkFragmentListBinding
import com.pluto.plugins.network.internal.ApiCallData
import com.pluto.plugins.network.internal.Session
import com.pluto.plugins.network.internal.interceptor.logic.NetworkCallsRepo
import com.pluto.plugins.network.internal.interceptor.ui.DetailsFragment.Companion.API_CALL_ID
import com.pluto.plugins.network.internal.interceptor.ui.list.NetworkAdapter
import com.pluto.utilities.autoClearInitializer
import com.pluto.utilities.extensions.hideKeyboard
import com.pluto.utilities.extensions.linearLayoutManager
import com.pluto.utilities.extensions.showMoreOptions
import com.pluto.utilities.list.BaseAdapter
import com.pluto.utilities.list.CustomItemDecorator
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.viewBinding

internal class ListFragment : Fragment(R.layout.pluto_network___fragment_list) {

    private val binding by viewBinding(PlutoNetworkFragmentListBinding::bind)
    private val viewModel: NetworkViewModel by activityViewModels()
    private val networkAdapter: BaseAdapter by autoClearInitializer {
        NetworkAdapter(onActionListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apiList.apply {
            adapter = networkAdapter
            addItemDecoration(CustomItemDecorator(requireContext()))
        }
        binding.search.doOnTextChanged { text, _, _, _ ->
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                text?.toString()?.let {
                    Session.lastSearchText = it
                    networkAdapter.list = filteredLogs(it)
                    if (it.isEmpty()) {
                        binding.apiList.linearLayoutManager()?.scrollToPositionWithOffset(0, 0)
                    }
                }
            }
        }
        binding.search.setText(Session.lastSearchText)
        binding.close.setOnDebounceClickListener {
            requireActivity().finish()
        }
        binding.options.setOnDebounceClickListener {
            requireContext().showMoreOptions(it, R.menu.pluto_network___menu_more_options) { item ->
                when (item.itemId) {
                    R.id.clear -> NetworkCallsRepo.deleteAll()
                    R.id.mock_settings -> findNavController().navigate(R.id.mockSettingsListView)
                }
            }
        }

        viewModel.apiCalls.removeObserver(listObserver)
        viewModel.apiCalls.observe(viewLifecycleOwner, listObserver)
    }

    private fun filteredLogs(search: String): List<ApiCallData> {
        var list = emptyList<ApiCallData>()
        viewModel.apiCalls.value?.let {
            list = it.filter { api ->
                api.request.url.contains(search, true) ||
                    api.request.graphqlData?.queryName?.contains(search, true) ?: false
            }
        }
        binding.noItemText.text = getString(
            if (search.isNotEmpty()) {
                R.string.pluto_network___no_search_result
            } else {
                R.string.pluto_network___no_api_text
            }
        )
        binding.noItemText.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        return list
    }

    private val listObserver = Observer<List<ApiCallData>> {
        networkAdapter.list = filteredLogs(binding.search.text.toString())
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder) {
            if (data is ApiCallData) {
                requireActivity().let {
                    it.hideKeyboard(viewLifecycleOwner.lifecycleScope) {
                        val bundle = bundleOf(API_CALL_ID to data.id)
                        findNavController().navigate(R.id.openDetails, bundle)
                    }
                }
            }
        }
    }
}
