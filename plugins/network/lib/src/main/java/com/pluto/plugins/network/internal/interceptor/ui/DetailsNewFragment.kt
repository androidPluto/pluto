package com.pluto.plugins.network.internal.interceptor.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pluto.plugin.KeyValuePairData
import com.pluto.plugin.utilities.extensions.asFormattedDate
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.onBackPressed
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugin.utilities.setOnDebounceClickListener
import com.pluto.plugin.utilities.spannable.createSpan
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugin.utilities.viewBinding
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkFragmentDetailsNewBinding
import com.pluto.plugins.network.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.internal.interceptor.logic.DetailContentData
import com.pluto.plugins.network.internal.interceptor.logic.NetworkViewModel
import com.pluto.plugins.network.internal.interceptor.logic.RequestData
import com.pluto.plugins.network.internal.interceptor.logic.ResponseData

class DetailsNewFragment : Fragment(R.layout.pluto_network___fragment_details_new) {

    private val binding by viewBinding(PlutoNetworkFragmentDetailsNewBinding::bind)
    private val viewModel: NetworkViewModel by activityViewModels()
    private lateinit var waitingText: CharSequence

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        waitingText = requireContext().createSpan {
            append(
                italic(
                    light(
                        fontColor(
                            requireContext().getString(R.string.pluto_network___waiting_for_response),
                            requireContext().color(R.color.pluto___text_dark_40)
                        )
                    )
                )
            )
        }
    }

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

    private val detailsObserver = Observer<DetailContentData> {
        binding.title.setSpan {
            append(fontColor(bold("${it.api.request.method.uppercase()},\t"), requireContext().color(R.color.pluto___white_60)))
            append(it.api.request.url.encodedPath)
        }
        setupStatusView(it.api)
        setupOverview(it.api)
        setupRequest(it.api.request)
        it.api.exception?.let { exception ->
//            setupException(exception)
        }
        it.api.response?.let { response ->
            setupResponse(response)
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

    private fun setupStatusView(data: ApiCallData) {
        binding.progress.visibility = View.VISIBLE
        binding.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.status.text = context?.getString(R.string.pluto_network___network_state_in_progress)
        binding.statusView.setBackgroundColor(requireContext().color(R.color.pluto___dark_05))

        data.exception?.let {
            binding.progress.visibility = View.GONE
            binding.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pluto_network___ic_error, 0, 0, 0)
            binding.status.setSpan {
                append(fontColor(it.name ?: context.getString(R.string.pluto_network___network_state_failed), context.color(R.color.pluto___red)))
            }
            binding.statusView.setBackgroundColor(requireContext().color(R.color.pluto___red_05))
        }

        data.response?.let {
            binding.progress.visibility = View.GONE
            binding.status.setCompoundDrawablesWithIntrinsicBounds(
                if (it.isSuccessful) R.drawable.pluto_network___ic_success else R.drawable.pluto_network___ic_error, 0, 0, 0
            )
            binding.status.setSpan {
                append(
                    fontColor(
                        bold("${it.status.code} "),
                        context.color(if (it.isSuccessful) R.color.pluto___dull_green else R.color.pluto___red)
                    )
                )
                append(
                    fontColor(
                        it.status.message,
                        context.color(if (it.isSuccessful) R.color.pluto___dull_green else R.color.pluto___red)
                    )
                )
            }
            binding.statusView.setBackgroundColor(requireContext().color(if (it.isSuccessful) R.color.pluto___dull_green_08 else R.color.pluto___red_05))
        }
    }

    private fun setupOverview(api: ApiCallData) {
        binding.overview.set(
            title = getString(R.string.pluto_network___tab_overview),
            keyValuePairs = arrayListOf<KeyValuePairData>().apply {
                add(
                    KeyValuePairData(
                        key = getString(R.string.pluto_network___url_label),
                        value = api.request.url.toString()
                    )
                )
                add(
                    KeyValuePairData(
                        key = getString(R.string.pluto_network___method_label),
                        value = api.request.method
                    )
                )
                add(
                    KeyValuePairData(
                        key = getString(R.string.pluto_network___ssl_label),
                        value = requireContext().createSpan { append(bold(api.request.url.isHttps.toString())) }
                    )
                )
                add(
                    KeyValuePairData(
                        key = getString(R.string.pluto_network___protocol_label),
                        value = generateProtocol(api)
                    )
                )
                add(
                    KeyValuePairData(
                        key = getString(R.string.pluto_network___request_time_label),
                        value = api.request.timestamp.asFormattedDate(DATE_FORMAT)
                    )
                )
                add(
                    KeyValuePairData(
                        key = getString(R.string.pluto_network___response_time_label),
                        value = (api.exception?.timeStamp ?: api.response?.receiveTimeMillis)?.asFormattedDate(DATE_FORMAT) ?: waitingText
                    )
                )
                add(
                    KeyValuePairData(
                        key = getString(R.string.pluto_network___delay_label),
                        value = api.responseTime?.let { "${it - api.request.timestamp} ms" } ?: run { waitingText }
                    )
                )
            }
        )
    }

    private fun setupResponse(response: ResponseData?) {
        binding.response.set(
            title = getString(R.string.pluto_network___tab_response),
            keyValuePairs = arrayListOf<KeyValuePairData>().apply {
                add(
                    KeyValuePairData(
                        key = getString(R.string.pluto_network___headers_title),
                        value = ""
                    )
                )
                add(
                    KeyValuePairData(
                        key = getString(R.string.pluto_network___body_title),
                        value = ""
                    )
                )
            }
        )
    }

    private fun setupRequest(request: RequestData) {
        binding.request.set(
            title = getString(R.string.pluto_network___tab_request),
            keyValuePairs = arrayListOf<KeyValuePairData>().apply {
                add(
                    KeyValuePairData(
                        key = getString(R.string.pluto_network___headers_title),
                        value = ""
                    )
                )
                add(
                    KeyValuePairData(
                        key = getString(R.string.pluto_network___query_params_title),
                        value = ""
                    )
                )
                add(
                    KeyValuePairData(
                        key = getString(R.string.pluto_network___body_title),
                        value = ""
                    )
                )
            }
        )
    }

    private fun generateProtocol(api: ApiCallData): CharSequence {
        return api.exception?.let {
            requireContext().createSpan { append(fontColor(context.getString(R.string.pluto_network___na), context.color(R.color.pluto___text_dark_40))) }
        } ?: run {
            api.response?.let {
                requireContext().createSpan {
                    append(semiBold(fontColor("${it.protocol}", context.color(R.color.pluto___text_dark_80))))
                    append(regular(fontColor(" (${it.protocol.name})", context.color(R.color.pluto___text_dark_60))))
                }
            } ?: run {
                waitingText
            }
        }
    }

    companion object {
        internal const val API_CALL_ID = "id"
        private const val DATE_FORMAT = "MMM dd, yyyy, HH:mm:ss.SSS"
    }
}
