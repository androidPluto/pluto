package com.mocklets.pluto.modules.network.ui.details

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mocklets.pluto.R
import com.mocklets.pluto.core.binding.viewBinding
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.ui.spannable.createSpan
import com.mocklets.pluto.core.ui.spannable.setSpan
import com.mocklets.pluto.databinding.PlutoFragmentNetworkDetailsResponseBinding
import com.mocklets.pluto.modules.exceptions.ExceptionData
import com.mocklets.pluto.modules.exceptions.ui.holder.CrashItemDetailsHeaderHolder
import com.mocklets.pluto.modules.network.ResponseData
import com.mocklets.pluto.modules.network.beautifyHeaders
import com.mocklets.pluto.modules.network.ui.DetailContentData
import com.mocklets.pluto.modules.network.ui.NetworkViewModel

internal class NetworkDetailsResponseFragment : Fragment(R.layout.pluto___fragment_network_details_response) {

    private val binding by viewBinding(PlutoFragmentNetworkDetailsResponseBinding::bind)
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
            it.api.response != null -> updateResponse(it.api.response!!, it.search)
            it.api.exception != null -> updateException(it.api.exception!!, it.search)
            else -> updateWaiting()
        }
    }

    private fun updateException(data: ExceptionData, search: String?) {
        binding.waiting.visibility = GONE
        binding.headerGroup.visibility = VISIBLE
        binding.bodyGroup.visibility = GONE

        binding.headerTitle.text = context?.getString(R.string.pluto___exception_title)
        setException(data, search)
    }

    private fun updateWaiting() {
        binding.waiting.visibility = VISIBLE
        binding.headerGroup.visibility = GONE
        binding.bodyGroup.visibility = GONE
    }

    private fun updateResponse(data: ResponseData, search: String?) {
        binding.waiting.visibility = GONE
        binding.headerGroup.visibility = VISIBLE
        binding.bodyGroup.visibility = GONE

        if (data.headers.isNotEmpty()) {
            binding.headerTitle.setSpan {
                append(context.getString(R.string.pluto___headers_title))
                append(fontColor(" (${data.headers.size})", context.color(R.color.pluto___text_dark_40)))
            }
            context?.beautifyHeaders(data.headers)?.let { binding.headers.setSpan { append(highlight(it, search)) } }
        }
        setBody(data, search)
    }

    private fun setBody(data: ResponseData, search: String?) {
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
            data.stackTrace.take(CrashItemDetailsHeaderHolder.MAX_STACK_TRACE_LINES).forEach {
                append("\n\t\t\t")
                append(fontColor(" at  ", context.color(R.color.pluto___text_dark_40)))
                append(highlight(it, search))
            }
            val extraTrace =
                data.stackTrace.size - CrashItemDetailsHeaderHolder.MAX_STACK_TRACE_LINES
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
}
