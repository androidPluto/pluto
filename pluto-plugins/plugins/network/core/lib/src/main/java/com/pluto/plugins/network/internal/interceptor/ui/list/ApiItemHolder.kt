package com.pluto.plugins.network.internal.interceptor.ui.list

import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkItemNetworkBinding
import com.pluto.plugins.network.intercept.NetworkData.Response
import com.pluto.plugins.network.internal.ApiCallData
import com.pluto.plugins.network.internal.interceptor.logic.RESPONSE_ERROR_STATUS_RANGE
import com.pluto.utilities.extensions.asTimeElapsed
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.inflate
import com.pluto.utilities.list.DiffAwareAdapter
import com.pluto.utilities.list.DiffAwareHolder
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.setSpan
import io.ktor.http.Url

internal class ApiItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto_network___item_network), actionListener) {

    private val binding = PlutoNetworkItemNetworkBinding.bind(itemView)
    private val host = binding.host
    private val url = binding.url
    private val progress = binding.progress
    private val status = binding.status
    private val error = binding.error
    private val timeElapsed = binding.timeElapsed
    private val proxyIndicator = binding.proxyIndicator
    private val graphqlIcon = binding.graphqlIcon

    override fun onBind(item: ListItem) {
        if (item is ApiCallData) {
            host.text = Url(item.request.url).host
            timeElapsed.text = item.request.sentTimestamp.asTimeElapsed()
            binding.root.setBackgroundColor(context.color(R.color.pluto___transparent))

            val method = (item.request.graphqlData?.queryType ?: item.request.method).uppercase()
            val urlOrQuery = item.request.graphqlData?.queryName ?: Url(item.request.url).encodedPath
            graphqlIcon.isVisible = item.request.graphqlData != null

            url.setSpan {
                append(fontColor(method, context.color(R.color.pluto___text_dark_60)))
                append("  $urlOrQuery")
            }
            progress.visibility = VISIBLE
            status.visibility = INVISIBLE
            error.visibility = INVISIBLE
            proxyIndicator.visibility = GONE

            item.exception?.let {
                handleExceptionUI()
            }

            item.mock?.let {
                proxyIndicator.visibility = VISIBLE
            }

            item.response?.let {
                handleResponseUI(it)
            }
            binding.root.setOnDebounceClickListener(DEBOUNCE_DELAY) {
                onAction("click")
            }
        }
    }

    private fun handleResponseUI(it: Response) {
        error.visibility = INVISIBLE
        progress.visibility = INVISIBLE
        status.visibility = VISIBLE
        status.text = it.status.code.toString()
        status.setTextColor(
            context.color(
                if (it.isSuccessful) {
                    R.color.pluto___dull_green
                } else {
                    if (it.status.code in RESPONSE_ERROR_STATUS_RANGE) {
                        R.color.pluto___orange
                    } else {
                        R.color.pluto___red
                    }
                }
            )
        )
        binding.root.setBackgroundColor(
            context.color(
                if (it.isSuccessful) {
                    R.color.pluto___dull_green_05
                } else {
                    if (it.status.code in RESPONSE_ERROR_STATUS_RANGE) {
                        R.color.pluto___orange_05
                    } else {
                        R.color.pluto___red_05
                    }
                }
            )
        )
    }

    private fun handleExceptionUI() {
        error.visibility = VISIBLE
        progress.visibility = INVISIBLE
        status.visibility = INVISIBLE
        binding.root.setBackgroundColor(context.color(R.color.pluto___red_05))
    }

    private companion object {
        const val DEBOUNCE_DELAY = 1_000L
    }
}
