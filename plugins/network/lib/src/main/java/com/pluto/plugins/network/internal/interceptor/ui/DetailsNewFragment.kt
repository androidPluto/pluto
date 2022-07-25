package com.pluto.plugins.network.internal.interceptor.ui

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.onBackPressed
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.sharing.Shareable
import com.pluto.plugin.utilities.sharing.lazyContentSharer
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkFragmentDetailsNewBinding
import com.pluto.plugins.network.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.internal.interceptor.logic.DetailContentData
import com.pluto.plugins.network.internal.interceptor.logic.NetworkViewModel

class DetailsNewFragment : Fragment(R.layout.pluto_network___fragment_details_new) {

    private val binding by viewBinding(PlutoNetworkFragmentDetailsNewBinding::bind)
    private val viewModel: NetworkViewModel by activityViewModels()
    private val contentSharer by lazyContentSharer()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed { findNavController().navigateUp() }
        setupControls()

        viewModel.detailContentLiveData.removeObserver(detailsObserver)
        viewModel.detailContentLiveData.observe(viewLifecycleOwner, detailsObserver)

        viewModel.apiCalls.removeObserver(listUpdateObserver)
        viewModel.apiCalls.observe(viewLifecycleOwner, listUpdateObserver)
    }

    private fun setupControls() {
        binding.close.setOnDebounceClickListener {
            requireActivity().onBackPressed()
        }
        binding.share.setOnDebounceClickListener {
            findNavController().navigate(R.id.openShareView)
        }
    }

    private fun handleUserAction(action: String, api: ApiCallData) {
        when (action) {
            ACTION_SHARE_CURL -> contentSharer.share(Shareable(title = "Share Request cURL", content = api.curl, fileName = "cURL Request from Pluto"))
            ACTION_OPEN_MOCK_SETTINGS -> findNavController().navigate(
                R.id.openMockSettingsEdit,
                bundleOf("url" to api.request.url.toString(), "method" to api.request.method)
            )
            ACTION_OPEN_REQ_HEADERS -> {
                requireContext().toast("req headers")
                findNavController().navigate(R.id.openContentFormatter)
            }
            ACTION_OPEN_REQ_PARAMS -> requireContext().toast("req params")
            ACTION_OPEN_REQ_BODY -> requireContext().toast("req body")
            ACTION_OPEN_RES_HEADERS -> requireContext().toast("res headers")
            ACTION_OPEN_RES_BODY -> requireContext().toast("res body")
        }
    }

    private val detailsObserver = Observer<DetailContentData> {
        binding.title.setSpan {
            append(fontColor(bold("${it.api.request.method.uppercase()}\t"), requireContext().color(R.color.pluto___white_60)))
            append(it.api.request.url.encodedPath)
        }
        binding.overview.apply {
            visibility = VISIBLE
            set(it.api) { action -> handleUserAction(action, it.api) }
        }
        binding.request.apply {
            visibility = VISIBLE
            set(it.api.request) { action -> handleUserAction(action, it.api) }
        }
        binding.response.apply {
            visibility = VISIBLE
            set(it.api.response, it.api.exception) { action -> handleUserAction(action, it.api) }
        }
    }

    private val listUpdateObserver = Observer<List<ApiCallData>> {
        val id = requireArguments().getString(API_CALL_ID, null)
        if (!id.isNullOrEmpty()) {
            viewModel.setCurrent(id)
        } else {
            requireContext().toast("invalid id")
            requireActivity().onBackPressed()
        }
    }

    companion object {
        internal const val API_CALL_ID = "id"
        internal const val ACTION_SHARE_CURL = "share_curl"
        internal const val ACTION_OPEN_MOCK_SETTINGS = "open_mock_settings"
        internal const val ACTION_OPEN_REQ_HEADERS = "open_request_headers"
        internal const val ACTION_OPEN_REQ_PARAMS = "open_request_params"
        internal const val ACTION_OPEN_REQ_BODY = "open_request_body"
        internal const val ACTION_OPEN_RES_HEADERS = "open_response_headers"
        internal const val ACTION_OPEN_RES_BODY = "open_response_body"
    }
}
