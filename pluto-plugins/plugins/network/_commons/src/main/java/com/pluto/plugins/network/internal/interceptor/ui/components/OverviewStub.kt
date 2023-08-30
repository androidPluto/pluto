package com.pluto.plugins.network.internal.interceptor.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugins.network.commons.R
import com.pluto.plugins.network.commons.databinding.PlutoNetworkStubDetailsOverviewBinding
import com.pluto.plugins.network.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.internal.interceptor.ui.DetailsFragment.Companion.ACTION_CUSTOM_TRACE_INFO
import com.pluto.plugins.network.internal.interceptor.ui.DetailsFragment.Companion.ACTION_OPEN_MOCK_SETTINGS
import com.pluto.plugins.network.internal.interceptor.ui.DetailsFragment.Companion.ACTION_SHARE_CURL
import com.pluto.utilities.extensions.asFormattedDate
import com.pluto.utilities.extensions.color
import com.pluto.utilities.setOnDebounceClickListener
import com.pluto.utilities.spannable.createSpan
import com.pluto.utilities.spannable.setSpan
import com.pluto.utilities.views.keyvalue.KeyValuePairData

internal class OverviewStub : ConstraintLayout {

    private val binding = PlutoNetworkStubDetailsOverviewBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    fun set(api: ApiCallData, onAction: (String) -> Unit) {
        setupStatusView(api)
        setupOverview(api, waitingText(context))
        handleMockSettings(onAction)
        handleCustomTraceInfo(api, onAction)
        binding.settingStub.copyCurl.setOnDebounceClickListener {
            onAction.invoke(ACTION_SHARE_CURL)
        }
        binding.settingStub.dividerTop.visibility = if (api.isCustomTrace) GONE else VISIBLE
        binding.settingStub.proxyRoot.visibility = if (api.isCustomTrace) GONE else VISIBLE
    }

    private fun handleCustomTraceInfo(api: ApiCallData, onAction: (String) -> Unit) {
        binding.customTraceTag.visibility = if (api.isCustomTrace) VISIBLE else GONE
        binding.customTraceTag.setSpan {
            append(underline(italic(context.getString(R.string.pluto_network___custom_trace_tag))))
        }
        binding.customTraceTag.setOnDebounceClickListener {
            onAction.invoke(ACTION_CUSTOM_TRACE_INFO)
        }
    }

    private fun handleMockSettings(onAction: (String) -> Unit) {
        binding.settingStub.proxyRoot.setOnDebounceClickListener {
            onAction.invoke(ACTION_OPEN_MOCK_SETTINGS)
        }
    }

    private fun setupStatusView(data: ApiCallData) {
        binding.progress.visibility = View.VISIBLE
        binding.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        binding.status.text = context?.getString(R.string.pluto_network___network_state_in_progress)
        binding.statusView.setBackgroundColor(context.color(R.color.pluto___dark_05))

        data.exception?.let {
            binding.progress.visibility = View.GONE
            binding.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pluto_network___ic_error, 0, 0, 0)
            binding.status.setSpan {
                append(fontColor(it.name ?: context.getString(R.string.pluto_network___network_state_failed), context.color(R.color.pluto___red_dark)))
            }
            binding.statusView.setBackgroundColor(context.color(R.color.pluto___red_05))
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
            binding.statusView.setBackgroundColor(context.color(if (it.isSuccessful) R.color.pluto___dull_green_08 else R.color.pluto___red_05))
        }
    }

    private fun setupOverview(api: ApiCallData, waitingText: CharSequence) {
        binding.table.set(
            title = context.getString(R.string.pluto_network___tab_overview),
            keyValuePairs = arrayListOf<KeyValuePairData>().apply {
                add(
                    KeyValuePairData(
                        key = context.getString(R.string.pluto_network___url_label),
                        value = api.request.url.toString()
                    )
                )
                add(
                    KeyValuePairData(
                        key = context.getString(R.string.pluto_network___method_label),
                        value = api.request.method
                    )
                )
                add(
                    KeyValuePairData(
                        key = context.getString(R.string.pluto_network___ssl_label),
                        value = context.createSpan { append(bold(api.request.url.startsWith("https").toString())) }
                    )
                )
                if (!api.isCustomTrace) {
                    add(
                        KeyValuePairData(
                            key = context.getString(R.string.pluto_network___protocol_label),
                            value = generateProtocol(api) ?: waitingText
                        )
                    )
                }
                add(
                    KeyValuePairData(
                        key = context.getString(R.string.pluto_network___request_time_label),
                        value = api.request.timestamp.asFormattedDate(DATE_FORMAT)
                    )
                )
                add(
                    KeyValuePairData(
                        key = context.getString(R.string.pluto_network___response_time_label),
                        value = (api.exception?.timeStamp ?: api.response?.receiveTimeMillis)?.asFormattedDate(
                            DATE_FORMAT
                        ) ?: waitingText
                    )
                )
                add(
                    KeyValuePairData(
                        key = context.getString(R.string.pluto_network___delay_label),
                        value = api.responseTime?.let { "${it - api.request.timestamp} ms" } ?: waitingText
                    )
                )
            }
        )
    }

    private fun generateProtocol(api: ApiCallData): CharSequence? {
        return api.exception?.let {
            context.createSpan { append(fontColor(context.getString(R.string.pluto_network___na), context.color(R.color.pluto___text_dark_40))) }
        } ?: run {
            api.response?.protocol?.let {
                context.createSpan {
                    append(semiBold(fontColor("$it", context.color(R.color.pluto___text_dark_80))))
                    append(regular(fontColor(" ($it)", context.color(R.color.pluto___text_dark_60))))
                }
            }
        }
    }

    companion object {
        private const val DATE_FORMAT = "MMM dd, yyyy, HH:mm:ss.SSS"
    }
}
