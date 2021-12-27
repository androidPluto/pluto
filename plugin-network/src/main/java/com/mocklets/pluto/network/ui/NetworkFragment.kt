package com.mocklets.pluto.network.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.mocklets.pluto.network.R
import com.mocklets.pluto.network.databinding.PlutoNetworkFragmentNetworkBinding
import com.mocklets.pluto.network.internal.ApiCallData
import com.mocklets.pluto.network.internal.NetworkCallsRepo
// import com.pluto.network.ui.details.NetworkCallDetailsFragment
import com.mocklets.pluto.plugin.utilities.extensions.hideKeyboard
import com.mocklets.pluto.plugin.utilities.extensions.linearLayoutManager
import com.mocklets.pluto.plugin.utilities.extensions.showMoreOptions
import com.mocklets.pluto.plugin.utilities.list.BaseAdapter
import com.mocklets.pluto.plugin.utilities.list.CustomItemDecorator
import com.mocklets.pluto.plugin.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.plugin.utilities.list.DiffAwareHolder
import com.mocklets.pluto.plugin.utilities.list.ListItem
// import com.pluto.utilities.routing.Screens
// import com.pluto.utilities.routing.lazyRouter
import com.mocklets.pluto.plugin.utilities.setDebounceClickListener
import com.mocklets.pluto.plugin.utilities.viewBinding

internal class NetworkFragment : Fragment(R.layout.pluto_network___fragment_network) {

    private val binding by viewBinding(PlutoNetworkFragmentNetworkBinding::bind)
    private val logsAdapter: BaseAdapter by lazy { NetworkAdapter(onActionListener) }
    private val viewModel: NetworkViewModel by activityViewModels()
//    private val router by lazyRouter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apiList.apply {
            adapter = logsAdapter
            addItemDecoration(CustomItemDecorator(requireContext()))
        }
        binding.search.addTextChangedListener { editable ->
            lifecycleScope.launchWhenResumed {
                editable?.toString()?.let {
//                    Pluto.session.networkSearchText = it
                    logsAdapter.list = filteredApi(it)
                    if (it.isEmpty()) {
                        binding.apiList.linearLayoutManager()?.scrollToPositionWithOffset(0, 0)
                    }
                }
            }
        }
        binding.more.setDebounceClickListener {
            requireContext().showMoreOptions(it, R.menu.pluto___popup_menu_network) { item ->
                when (item.itemId) {
//                    R.id.proxy_settings -> router.navigate(Screens.NetworkProxySettingsList)
                    R.id.clear -> NetworkCallsRepo.deleteAll()
                }
            }
        }
        // todo
//        binding.search.setText(Pluto.session.networkSearchText)
        viewModel.apiCalls.removeObserver(networkObserver)
        viewModel.apiCalls.observe(viewLifecycleOwner, networkObserver)
    }

    private fun filteredApi(search: String): List<ApiCallData> {
        val list = (viewModel.apiCalls.value ?: emptyList()).filter { api ->
            api.request.url.toString().contains(search, true)
        }
        binding.noItemText.text = getString(
            if (search.isNotEmpty()) R.string.pluto_network___no_search_result else R.string.pluto_network___no_api_text
        )
        binding.noItemText.visibility = if (list.isEmpty()) VISIBLE else GONE
        return list
    }

    private val networkObserver = Observer<List<ApiCallData>> {
        logsAdapter.list = filteredApi(binding.search.text.toString())
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            if (data is ApiCallData) {
                activity!!.hideKeyboard()
//                router.navigate(Screens.NetworkCallDetails(NetworkCallDetailsFragment.Data(data.id)))
            }
        }
    }
}
