package com.pluto.plugins.network.internal.interceptor.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.pluto.plugin.KeyValuePairData
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.toast
import com.pluto.plugin.utilities.spannable.createSpan
import com.pluto.plugins.network.R
import com.pluto.plugins.network.databinding.PlutoNetworkStubDetailsRequestBinding
import com.pluto.plugins.network.internal.interceptor.logic.RequestData
import com.pluto.plugins.network.internal.interceptor.logic.formatSizeAsBytes

internal class RequestStub : ConstraintLayout {

    private val binding = PlutoNetworkStubDetailsRequestBinding.inflate(LayoutInflater.from(context), this, true)
    private val tapHelperText = tapIndicatorText(context)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0)
    constructor(context: Context) : super(context, null, 0)

    fun set(request: RequestData) {
        binding.table.set(
            title = context.getString(R.string.pluto_network___tab_request),
            keyValuePairs = arrayListOf<KeyValuePairData>().apply {
                add(getHeadersData(request))
                add(getQueryParamsData(request))
                add(getBodyData(request))
            }
        )
    }

    private fun getBodyData(request: RequestData) = KeyValuePairData(
        key = context.getString(R.string.pluto_network___body_title),
        value = context.createSpan {
            request.body?.let {
                if (it.isBinary) {
                    append(binaryBodyText(context))
                } else {
                    if (request.bodySize > 0) {
                        append(semiBold(formatSizeAsBytes(it.body?.length?.toLong() ?: 0L)))
                        append(tapHelperText)
                    } else {
                        append(fontColor("0 byte", context.color(R.color.pluto___text_dark_40)))
                    }
                }
            } ?: run {
                append(fontColor("--", context.color(R.color.pluto___text_dark_40)))
            }
        },
        showClickIndicator = request.body?.isBinary != true && request.bodySize > 0,
        onClick = request.body?.let {
            if (it.isBinary) {
                null
            } else {
                if (request.bodySize > 0) {
                    { context.toast("body clicked") }
                } else {
                    null
                }
            }
        }
    )

    private fun getQueryParamsData(request: RequestData) = KeyValuePairData(
        key = context.getString(R.string.pluto_network___query_params_title),
        value = context.createSpan {
            if (request.url.querySize > 0) {
                append(semiBold("${request.url.querySize} params"))
                append(tapHelperText)
            } else {
                append(fontColor("--", context.color(R.color.pluto___text_dark_40)))
            }
        },
        showClickIndicator = request.url.querySize > 0,
        onClick = if (request.url.querySize > 0) {
            { }
        } else {
            null
        }
    )

    private fun getHeadersData(request: RequestData) = KeyValuePairData(
        key = context.getString(R.string.pluto_network___headers_title),
        value = context.createSpan {
            if (request.headers.isNotEmpty()) {
                append(semiBold("${request.headers.size} items"))
                append(tapHelperText)
            } else {
                append(fontColor("--", context.color(R.color.pluto___text_dark_40)))
            }
        },
        showClickIndicator = true,
        onClick = {
        }
    )
}
