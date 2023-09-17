package com.pluto.plugins.network.internal.interceptor.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkStubDetailsOverviewBinding
import com.pluto.plugins.network.internal.ApiCallData
import com.pluto.utilities.extensions.asFormattedDate
import com.pluto.utilities.extensions.color
import com.pluto.utilities.spannable.createSpan
import com.pluto.utilities.views.keyvalue.KeyValuePairData

internal class OverviewStub : ConstraintLayout {

    private val binding = PlutoNetworkStubDetailsOverviewBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    fun set(api: ApiCallData) {
//        setupStatusView(api)
        setupOverview(api, waitingText(context))
//        handleMockSettings(onAction)
//        handleCustomTraceInfo(api, onAction)
    }

//    private fun handleCustomTraceInfo(api: ApiCallData, onAction: (String) -> Unit) {
//        binding.customTraceTag.visibility = if (api.isCustomTrace) VISIBLE else GONE
//        binding.customTraceTag.setSpan {
//            append(underline(italic(context.getString(R.string.pluto_network___custom_trace_tag))))
//        }
//        binding.customTraceTag.setOnDebounceClickListener {
//            onAction.invoke(ACTION_CUSTOM_TRACE_INFO)
//        }
//    }

//    private fun setupStatusView(data: ApiCallData) {
//        binding.progress.visibility = View.VISIBLE
//        binding.status.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
//        binding.status.text = context?.getString(R.string.pluto_network___network_state_in_progress)
//        binding.statusView.setBackgroundColor(context.color(R.color.pluto___dark_05))
//
//        data.exception?.let {
//            binding.progress.visibility = View.GONE
//            binding.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pluto_network___ic_error, 0, 0, 0)
//            binding.status.setSpan {
//                append(fontColor(it.name ?: context.getString(R.string.pluto_network___network_state_failed), context.color(R.color.pluto___red_dark)))
//            }
//            binding.statusView.setBackgroundColor(context.color(R.color.pluto___red_05))
//        }
//
//        data.response?.let {
//            binding.progress.visibility = View.GONE
//            binding.status.setCompoundDrawablesWithIntrinsicBounds(
//                if (it.isSuccessful) R.drawable.pluto_network___ic_success else R.drawable.pluto_network___ic_error, 0, 0, 0
//            )
//            binding.status.setSpan {
//                append(
//                    fontColor(
//                        bold("${it.status.code} "),
//                        context.color(if (it.isSuccessful) R.color.pluto___dull_green else R.color.pluto___red)
//                    )
//                )
//                append(
//                    fontColor(
//                        it.status.message,
//                        context.color(if (it.isSuccessful) R.color.pluto___dull_green else R.color.pluto___red)
//                    )
//                )
//            }
//            binding.statusView.setBackgroundColor(context.color(if (it.isSuccessful) R.color.pluto___dull_green_08 else R.color.pluto___red_05))
//        }
//    }

    private fun setupOverview(api: ApiCallData, waitingText: CharSequence) {
        binding.table.set(
            title = context.getString(R.string.pluto_network___tab_overview),
            keyValuePairs = arrayListOf<KeyValuePairData>().apply {
//                add(
//                    KeyValuePairData(
//                        key = context.getString(R.string.pluto_network___url_label),
//                        value = api.request.url
//                    )
//                )
//                add(
//                    KeyValuePairData(
//                        key = context.getString(R.string.pluto_network___method_label),
//                        value = api.request.method
//                    )
//                )
                add(
                    KeyValuePairData(
                        key = context.getString(R.string.pluto_network___ssl_label),
                        value = context.createSpan { append(bold(api.request.url.startsWith("https").toString())) }
                    )
                )
                add(
                    KeyValuePairData(
                        key = context.getString(R.string.pluto_network___protocol_label),
                        value = generateProtocol(api) ?: waitingText
                    )
                )
                add(
                    KeyValuePairData(
                        key = context.getString(R.string.pluto_network___request_time_label),
                        value = api.request.sentTimestamp.asFormattedDate(DATE_FORMAT)
                    )
                )
                add(
                    KeyValuePairData(
                        key = context.getString(R.string.pluto_network___response_time_label),
                        value = (api.exception?.timeStamp ?: api.response?.receiveTimestamp)?.asFormattedDate(
                            DATE_FORMAT
                        ) ?: waitingText
                    )
                )
                add(
                    KeyValuePairData(
                        key = context.getString(R.string.pluto_network___delay_label),
                        value = api.responseTime?.let { "${it - api.request.sentTimestamp} ms" } ?: waitingText
                    )
                )
                add(
                    KeyValuePairData(
                        key = context.getString(R.string.pluto_network___interceptor_type_label),
                        value = context.createSpan { append(semiBold(api.interceptorOption.name)) }
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
                    if (it.isBlank()) {
                        append(fontColor(context.getString(R.string.pluto_network___na), context.color(R.color.pluto___text_dark_40)))
                    } else {
                        append(semiBold(fontColor(it, context.color(R.color.pluto___text_dark_80))))
                    }
                }
            }
        }
    }

    companion object {
        private const val DATE_FORMAT = "MMM dd, yyyy, HH:mm:ss.SSS"
    }
}
