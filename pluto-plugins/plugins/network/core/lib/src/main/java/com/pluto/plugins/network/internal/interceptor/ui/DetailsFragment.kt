package com.pluto.plugins.network.internal.interceptor.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.share.Shareable
import com.pluto.plugin.share.lazyContentSharer
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkFragmentDetailsBinding
import com.pluto.plugins.network.intercept.NetworkData
import com.pluto.plugins.network.internal.ApiCallData
import com.pluto.plugins.network.internal.interceptor.logic.RESPONSE_ERROR_STATUS_RANGE
import com.pluto.plugins.network.internal.interceptor.logic.beautify
import com.pluto.plugins.network.internal.interceptor.logic.beautifyHeaders
import com.pluto.plugins.network.internal.interceptor.logic.beautifyQueryParams
import com.pluto.plugins.network.internal.interceptor.logic.formatSizeAsBytes
import com.pluto.plugins.network.internal.interceptor.ui.ContentFragment.Companion.DATA
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.onBackPressed
import com.pluto.utilities.extensions.toast
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan
import com.pluto.utilities.viewBinding
import io.ktor.http.Url

internal class DetailsFragment : Fragment(R.layout.pluto_network___fragment_details) {

    private val binding by viewBinding(PlutoNetworkFragmentDetailsBinding::bind)
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

            ACTION_OPEN_REQ_HEADERS -> openContentView(
                title = getString(R.string.pluto_network___content_request_headers),
                content = requireContext().beautifyHeaders(api.request.headers),
                sizeText = "${api.request.headers.size} items"
            )

            ACTION_OPEN_REQ_PARAMS -> openContentView(
                title = getString(R.string.pluto_network___content_request_query_param),
                content = requireContext().beautifyQueryParams(api.request.url),
                sizeText = "${Url(api.request.url).parameters.names().count()} items"
            )

            ACTION_OPEN_REQ_BODY -> api.request.body?.let {
                openContentView(
                    title = getString(R.string.pluto_network___content_request_body),
                    content = it.beautify(),
                    typeText = it.mediaTypeFull,
                    sizeText = formatSizeAsBytes(it.sizeInBytes),
                    isTreeViewAllowed = true
                )
            }

            ACTION_OPEN_RES_HEADERS -> api.response?.headers?.let {
                openContentView(
                    title = getString(R.string.pluto_network___content_response_headers),
                    content = requireContext().beautifyHeaders(it),
                    sizeText = "${it.size} items"
                )
            }

            ACTION_OPEN_RES_BODY -> api.response?.body?.let {
                openContentView(
                    title = getString(R.string.pluto_network___content_response_body),
                    content = it.beautify(),
                    typeText = it.mediaTypeFull,
                    sizeText = formatSizeAsBytes(it.sizeInBytes),
                    isTreeViewAllowed = true
                )
            }

            ACTION_CUSTOM_TRACE_INFO -> findNavController().navigate(R.id.openCustomTraceInfo)
        }
    }

    private fun openContentView(title: String, content: CharSequence, typeText: String? = null, sizeText: String, isTreeViewAllowed: Boolean = false) {
        findNavController().navigate(
            R.id.openContentFormatter,
            bundleOf(
                DATA to ContentFormatterData(
                    title = title,
                    content = content,
                    typeText = typeText,
                    sizeText = sizeText,
                    isTreeViewAllowed = isTreeViewAllowed
                )
            )
        )
    }

    private val detailsObserver = Observer<DetailContentData> {
        setupStatusView(it.api)
        val graphqlData = it.api.request.graphqlData
        binding.graphqlIcon.isVisible = graphqlData != null
        if (graphqlData != null) {
            binding.method.text = "${graphqlData.queryType.uppercase()} ${graphqlData.queryName}"
            binding.url.text = graphqlData.variables.toString()
        } else {
            binding.method.text = it.api.request.method.uppercase()
            binding.url.text = Url(it.api.request.url).toString()
        }

        binding.overview.apply {
            visibility = VISIBLE
            set(it.api)
        }
        handleCtas { action -> handleUserAction(action, it.api) }
        binding.request.apply {
            visibility = VISIBLE
            set(it.api.request) { action -> handleUserAction(action, it.api) }
        }
        binding.response.apply {
            visibility = VISIBLE
            set(it.api.response, it.api.exception) { action -> handleUserAction(action, it.api) }
        }
    }

    private fun handleCtas(onAction: (String) -> Unit) {
        binding.settingStub.proxyRoot.setOnDebounceClickListener {
            onAction.invoke(ACTION_OPEN_MOCK_SETTINGS)
        }
        binding.settingStub.copyCurl.setOnDebounceClickListener { _ ->
            onAction.invoke(ACTION_SHARE_CURL)
        }
        binding.settingStub.dividerTop.visibility = VISIBLE
        binding.settingStub.proxyRoot.visibility = VISIBLE
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

    private fun setupStatusView(data: ApiCallData) {
        binding.progress.visibility = VISIBLE
        binding.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.status.setSpan {
            append(italic(fontColor(context.getString(R.string.pluto_network___network_state_in_progress), context.color(R.color.pluto___white_60))))
        }

        data.exception?.let {
            binding.progress.visibility = GONE
            binding.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pluto_network___ic_error, 0, 0, 0)
            binding.status.setSpan {
                append(bold(fontColor(context.getString(R.string.pluto_network___network_state_failed), context.color(R.color.pluto___red))))
            }
        }

        data.response?.let {
            binding.progress.visibility = GONE
            binding.status.setCompoundDrawablesWithIntrinsicBounds(
                getErrorIcon(it), 0, 0, 0
            )
            binding.status.setSpan {
                append(fontColor(bold(it.status.code.toString()), context.color(getStatusTextColorId(it))))
                append(italic(fontColor(" ${it.status.message} ", context.color(getStatusTextColorId(it)))))
            }
        }
    }

    private fun getStatusTextColorId(it: NetworkData.Response): Int {
        return if (it.isSuccessful) {
            R.color.pluto___dull_green
        } else {
            if (it.status.code in RESPONSE_ERROR_STATUS_RANGE) {
                R.color.pluto___orange
            } else {
                R.color.pluto___red
            }
        }
    }

    private fun getErrorIcon(it: NetworkData.Response): Int {
        return if (it.isSuccessful) {
            R.drawable.pluto_network___ic_success
        } else {
            if (it.status.code in RESPONSE_ERROR_STATUS_RANGE) {
                R.drawable.pluto_network___ic_error_orange
            } else {
                R.drawable.pluto_network___ic_error
            }
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
        internal const val ACTION_CUSTOM_TRACE_INFO = "custom_trace_info"
    }
}
