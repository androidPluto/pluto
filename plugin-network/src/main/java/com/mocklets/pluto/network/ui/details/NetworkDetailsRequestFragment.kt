package com.mocklets.pluto.network.ui.details

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mocklets.pluto.network.R
import com.mocklets.pluto.network.databinding.PlutoNetworkFragmentNetworkDetailsRequestBinding
import com.mocklets.pluto.network.internal.RequestData
import com.mocklets.pluto.network.internal.beautifyHeaders
import com.mocklets.pluto.network.internal.beautifyQueryParams
import com.mocklets.pluto.network.ui.DetailContentData
import com.mocklets.pluto.network.ui.NetworkViewModel
import com.mocklets.pluto.plugin.utilities.extensions.color
import com.mocklets.pluto.plugin.utilities.spannable.setSpan
import com.mocklets.pluto.plugin.utilities.viewBinding

internal class NetworkDetailsRequestFragment : Fragment(R.layout.pluto_network___fragment_network_details_request) {

    private val binding by viewBinding(PlutoNetworkFragmentNetworkDetailsRequestBinding::bind)
    private val viewModel: NetworkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.detailContentLiveData.removeObservers(viewLifecycleOwner)
        viewModel.detailContentLiveData.observe(viewLifecycleOwner, apiCallObserver)
    }

    private val apiCallObserver = Observer<DetailContentData> {
        updateUi(it.api.request, it.search)
    }

    private fun updateUi(data: RequestData, search: String?) {
        if (data.headers.isNotEmpty()) {
            binding.headerTitle.setSpan {
                append(context.getString(R.string.pluto_network___headers_title))
                append(fontColor(" (${data.headers.size})", context.color(R.color.pluto___text_dark_40)))
            }
            context?.beautifyHeaders(data.headers)?.let { binding.headers.setSpan { append(highlight(it, search)) } }
        }

        val queryParamsText = context?.beautifyQueryParams(data.url)
        if (!queryParamsText.isNullOrEmpty()) {
            binding.queryParamsGroup.visibility = VISIBLE
            binding.queryParamsTitle.setSpan {
                append(context.getString(R.string.pluto_network___query_params_title))
                append(fontColor(" (${data.url.querySize()})", context.color(R.color.pluto___text_dark_40)))
            }
            binding.queryParams.setSpan { append(highlight(queryParamsText, search)) }
        } else {
            binding.queryParamsGroup.visibility = GONE
        }
        setBody(data, search)
    }

    private fun setBody(data: RequestData, search: String?) {
        binding.bodyGroup.visibility = GONE
        data.body?.let {
            if (it.isValid) {
                binding.bodyGroup.visibility = VISIBLE
                binding.body.setSpan {
                    if (it.isBinary) {
                        append(fontColor(italic("${it.body}"), context.color(R.color.pluto___text_dark_60)))
                    } else {
                        append(highlight("${it.body}", search))
                    }
                }
            }
        }
    }
}
