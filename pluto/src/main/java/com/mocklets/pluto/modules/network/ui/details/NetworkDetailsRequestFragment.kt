package com.mocklets.pluto.modules.network.ui.details

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.ui.spannable.setSpan
import com.mocklets.pluto.core.viewBinding
import com.mocklets.pluto.databinding.PlutoFragmentNetworkDetailsRequestBinding
import com.mocklets.pluto.modules.network.RequestData
import com.mocklets.pluto.modules.network.beautifyHeaders
import com.mocklets.pluto.modules.network.beautifyQueryParams
import com.mocklets.pluto.modules.network.ui.DetailContentData
import com.mocklets.pluto.modules.network.ui.NetworkViewModel

internal class NetworkDetailsRequestFragment : Fragment(R.layout.pluto___fragment_network_details_request) {

    private val binding by viewBinding(PlutoFragmentNetworkDetailsRequestBinding::bind)
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
                append(context.getString(R.string.pluto___headers_title))
                append(fontColor(" (${data.headers.size})", context.color(R.color.pluto___text_dark_40)))
            }
            context?.beautifyHeaders(data.headers)?.let { binding.headers.setSpan { append(highlight(it, search)) } }
        }

        val queryParamsText = context?.beautifyQueryParams(data.url)
        if (!queryParamsText.isNullOrEmpty()) {
            binding.queryParamsGroup.visibility = VISIBLE
            binding.queryParamsTitle.setSpan {
                append(context.getString(R.string.pluto___query_params_title))
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
