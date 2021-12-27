package com.mocklets.pluto.network.ui.details

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mocklets.pluto.network.R
import com.mocklets.pluto.network.databinding.PlutoNetworkFragmentNetworkDetailsResponseBinding
import com.mocklets.pluto.network.internal.ExceptionData
import com.mocklets.pluto.network.internal.ResponseData
import com.mocklets.pluto.network.internal.beautifyHeaders
import com.mocklets.pluto.network.ui.DetailContentData
import com.mocklets.pluto.network.ui.NetworkViewModel
import com.mocklets.pluto.plugin.utilities.extensions.color
import com.mocklets.pluto.plugin.utilities.spannable.createSpan
import com.mocklets.pluto.plugin.utilities.spannable.setSpan
import com.mocklets.pluto.plugin.utilities.viewBinding

internal class NetworkDetailsResponseFragment : Fragment(R.layout.pluto_network___fragment_network_details_response) {

    private val binding by viewBinding(PlutoNetworkFragmentNetworkDetailsResponseBinding::bind)
    private val viewModel: NetworkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.waiting.visibility = GONE
        binding.headerGroup.visibility = GONE
        binding.bodyGroup.visibility = GONE

        viewModel.detailContentLiveData.removeObservers(viewLifecycleOwner)
        viewModel.detailContentLiveData.observe(viewLifecycleOwner, apiCallObserver)
    }

    private val apiCallObserver = Observer<DetailContentData> {
        when {
            it.api.response != null -> updateResponse(it.api.response!!, it.search, it.api.hasResponseBody)
            it.api.exception != null -> updateException(it.api.exception!!, it.search)
            else -> updateWaiting()
        }
    }

    private fun updateException(data: ExceptionData, search: String?) {
        binding.waiting.visibility = GONE
        binding.headerGroup.visibility = VISIBLE
        binding.bodyGroup.visibility = GONE

        binding.headerTitle.text = context?.getString(R.string.pluto_network___exception_title)
        setException(data, search)
    }

    private fun updateWaiting() {
        binding.waiting.visibility = VISIBLE
        binding.headerGroup.visibility = GONE
        binding.bodyGroup.visibility = GONE
    }

    private fun updateResponse(data: ResponseData, search: String?, hasBody: Boolean) {
        binding.waiting.visibility = GONE
        binding.headerGroup.visibility = VISIBLE
        binding.bodyGroup.visibility = GONE

        if (data.headers.isNotEmpty()) {
            binding.headerTitle.setSpan {
                append(context.getString(R.string.pluto_network___headers_title))
                append(fontColor(" (${data.headers.size})", context.color(R.color.pluto___text_dark_40)))
            }
            context?.beautifyHeaders(data.headers)?.let { binding.headers.setSpan { append(highlight(it, search)) } }
        }
        setBody(data, search, hasBody)
    }

    private fun setBody(data: ResponseData, search: String?, hasBody: Boolean) {
        if (hasBody) {
            binding.bodyGroup.visibility = VISIBLE
            binding.body.setSpan {
                append(fontColor(italic(context.getString(R.string.pluto_network___processing_body)), context.color(R.color.pluto___text_dark_40)))
            }
            data.body?.let {
                if (it.isValid) {
                    binding.bodyGroup.visibility = VISIBLE
                    binding.body.setSpan {
                        if (it.isBinary) {
                            append(fontColor(italic("${it.body}"), context.color(R.color.pluto___text_dark_60)))
                        } else {
                            if (it.body?.length ?: 0 > MAX_BLOB_LENGTH) {
                                append(highlight("${it.body?.subSequence(0, MAX_BLOB_LENGTH)}", search))
                                append("\n\n\t")
                                append(
                                    fontColor(
                                        italic(bold(context.getString(R.string.pluto_network___content_truncated))),
                                        context.color(R.color.pluto___text_dark_40)
                                    )
                                )
                            } else {
                                append(highlight("${it.body}", search))
                            }
                        }
                    }
                } else {
                    binding.bodyGroup.visibility = GONE
                }
            }
        }
    }

    private fun setException(data: ExceptionData, search: String?) {
        binding.headerTitle.setSpan {
            val exceptionData = context.createSpan {
                append(
                    highlight(
                        semiBold(fontColor("${data.name}\n", context.color(R.color.pluto___text_dark_80))),
                        search
                    )
                )
                data.message?.let { append(highlight(semiBold(it), search)) }
            }
            append(highlight(exceptionData, search))
        }
        binding.headers.setSpan {
            append(highlight("${data.name}: ${data.message}", search))
            data.stackTrace.take(MAX_STACK_TRACE_LINES).forEach {
                append("\n\t\t\t")
                append(fontColor(" at  ", context.color(R.color.pluto___text_dark_40)))
                append(highlight(it, search))
            }
            val extraTrace =
                data.stackTrace.size - MAX_STACK_TRACE_LINES
            if (extraTrace > 0) {
                append(
                    fontColor(
                        "\n\t\t\t + $extraTrace more lines",
                        context.color(R.color.pluto___text_dark_40)
                    )
                )
            }
        }
    }

    private companion object {
        const val MAX_BLOB_LENGTH = 25_000
        const val MAX_STACK_TRACE_LINES = 15
    }
}
