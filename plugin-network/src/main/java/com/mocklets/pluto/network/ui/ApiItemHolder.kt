package com.mocklets.pluto.network.ui

import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.mocklets.pluto.network.R
import com.mocklets.pluto.network.databinding.PlutoNetworkItemNetworkBinding
import com.mocklets.pluto.network.internal.ApiCallData
import com.mocklets.pluto.network.internal.ResponseData
import com.mocklets.pluto.network.internal.hostUrl
import com.mocklets.pluto.plugin.utilities.extensions.asTimeElapsed
import com.mocklets.pluto.plugin.utilities.extensions.color
import com.mocklets.pluto.plugin.utilities.extensions.inflate
import com.mocklets.pluto.plugin.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.plugin.utilities.list.DiffAwareHolder
import com.mocklets.pluto.plugin.utilities.list.ListItem
import com.mocklets.pluto.plugin.utilities.setDebounceClickListener
import com.mocklets.pluto.plugin.utilities.spannable.setSpan

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

    override fun onBind(item: ListItem) {
        if (item is ApiCallData) {
            host.text = item.request.url.hostUrl()
            timeElapsed.text = item.request.timestamp.asTimeElapsed()
            itemView.setBackgroundColor(itemView.context.color(R.color.pluto___transparent))

            val endPoint = StringBuilder()
            item.request.url.pathSegments().forEach { segment ->
                endPoint.append("/$segment")
            }
            url.setSpan {
                append(fontColor(item.request.method.uppercase(), context.color(R.color.pluto___text_dark_60)))
                append("  $endPoint")
            }
            progress.visibility = VISIBLE
            status.visibility = INVISIBLE
            error.visibility = INVISIBLE
            proxyIndicator.visibility = GONE

            item.exception?.let {
                handleExceptionUI()
            }

            item.proxy?.let {
                proxyIndicator.visibility = VISIBLE
            }

            item.response?.let {
                handleResponseUI(it)
            }
            itemView.setDebounceClickListener(DEBOUNCE_DELAY) {
                onAction("click")
            }
        }
    }

    private fun handleResponseUI(it: ResponseData) {
        error.visibility = INVISIBLE
        progress.visibility = INVISIBLE
        status.visibility = VISIBLE
        status.text = it.status.code.toString()
        status.setTextColor(
            itemView.context.color(
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
        itemView.setBackgroundColor(
            itemView.context.color(
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
        itemView.setBackgroundColor(itemView.context.color(R.color.pluto___red_05))
    }

    private companion object {
        const val DEBOUNCE_DELAY = 1_000L
        val RESPONSE_ERROR_STATUS_RANGE = 400..499
    }
}
