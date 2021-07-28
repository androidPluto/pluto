package com.mocklets.pluto.modules.network.ui

import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.asTimeElapsed
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.core.ui.spannable.setSpan
import com.mocklets.pluto.databinding.PlutoItemNetworkBinding
import com.mocklets.pluto.modules.network.ApiCallData
import com.mocklets.pluto.modules.network.ResponseData
import com.mocklets.pluto.modules.network.hostUrl

internal class ApiItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_network), actionListener) {

    private val binding = PlutoItemNetworkBinding.bind(itemView)
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
