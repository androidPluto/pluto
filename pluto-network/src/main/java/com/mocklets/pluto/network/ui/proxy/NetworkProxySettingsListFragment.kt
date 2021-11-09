package com.mocklets.pluto.network.ui.proxy

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.mocklets.pluto.network.R
import com.mocklets.pluto.network.databinding.PlutoNetworkFragmentNetworkProxySettingsListBinding
import com.mocklets.pluto.network.internal.proxy.NetworkProxyViewModel
import com.mocklets.pluto.network.internal.proxy.dao.NetworkProxyEntity
import com.mocklets.pluto.utilities.extensions.dp
import com.mocklets.pluto.utilities.extensions.hideKeyboard
import com.mocklets.pluto.utilities.extensions.showKeyboard
import com.mocklets.pluto.utilities.list.BaseAdapter
import com.mocklets.pluto.utilities.list.CustomItemDecorator
import com.mocklets.pluto.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.utilities.list.DiffAwareHolder
import com.mocklets.pluto.utilities.list.ListItem
import com.mocklets.pluto.utilities.routing.OnBackKeyHandler
import com.mocklets.pluto.utilities.routing.lazyRouter
import com.mocklets.pluto.utilities.setDebounceClickListener
import com.mocklets.pluto.utilities.viewBinding

internal class NetworkProxySettingsListFragment :
    Fragment(R.layout.pluto_network___fragment_network_proxy_settings_list),
    OnBackKeyHandler {

    private val binding by viewBinding(PlutoNetworkFragmentNetworkProxySettingsListBinding::bind)
    private val networkProxyAdapter: BaseAdapter by lazy { NetworkProxyItemAdapter(onActionListener) }
    private val viewModel: NetworkProxyViewModel by activityViewModels()
    private val router by lazyRouter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.apply {
            adapter = networkProxyAdapter
            addItemDecoration(CustomItemDecorator(context, DECORATOR_DIVIDER_PADDING))
        }
        binding.close.setDebounceClickListener {
            activity?.onBackPressed()
        }
        binding.search.setDebounceClickListener {
            binding.searchView.visibility = VISIBLE
            binding.searchView.requestFocus()
        }
        binding.closeSearch.setDebounceClickListener {
            exitSearch()
        }
        binding.clearSearch.setDebounceClickListener {
            binding.editSearch.text = null
        }
        binding.editSearch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.showKeyboard()
            } else {
                v.hideKeyboard()
            }
        }
        binding.editSearch.addTextChangedListener { editable ->
            lifecycleScope.launchWhenResumed {
                editable?.toString()?.let {
                    viewModel.fetchList(it)
                }
            }
        }
        binding.create.setDebounceClickListener {
            router.navigate(Screens.NetworkProxySettings())
        }

        viewModel.proxyList.removeObserver(networkProxyObserver)
        viewModel.proxyList.observe(viewLifecycleOwner, networkProxyObserver)
        viewModel.fetchList()
    }

    private val networkProxyObserver = Observer<List<NetworkProxyEntity>> {
        networkProxyAdapter.list = it
        binding.noNetworkProxy.visibility = if (it.isEmpty()) VISIBLE else GONE
    }

    override fun onBackPressed(): Boolean {
        if (binding.searchView.isVisible) {
            exitSearch()
            return true
        }
        return false
    }

    private fun exitSearch() {
        binding.editSearch.text = null
        binding.searchView.visibility = GONE
        binding.editSearch.clearFocus()
    }

    private val onActionListener = object : DiffAwareAdapter.OnActionListener {
        override fun onAction(action: String, data: ListItem, holder: DiffAwareHolder?) {
            when (data) {
                is NetworkProxyEntity -> router.navigate(
                    Screens.NetworkProxySettings(
                        NetworkProxySettingsFragment.Data(
                            data.requestUrl,
                            data.requestMethod
                        )
                    )
                )
            }
        }
    }

    private companion object {
        val DECORATOR_DIVIDER_PADDING = 16f.dp.toInt()
    }
}
