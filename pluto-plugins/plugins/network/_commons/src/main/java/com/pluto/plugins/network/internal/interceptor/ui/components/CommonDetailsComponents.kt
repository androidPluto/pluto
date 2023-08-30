package com.pluto.plugins.network.internal.interceptor.ui.components

import android.content.Context
import com.pluto.plugins.network.commons.R
import com.pluto.plugins.network.internal.interceptor.logic.ProcessedBody
import com.pluto.plugins.network.internal.interceptor.logic.core.asUrl
import com.pluto.plugins.network.internal.interceptor.logic.formatSizeAsBytes
import com.pluto.utilities.extensions.color
import com.pluto.utilities.spannable.createSpan
import com.pluto.utilities.views.keyvalue.KeyValuePairData
import io.ktor.http.Url

internal fun waitingText(context: Context) = context.createSpan {
    append(
        italic(
            light(
                fontColor(
                    context.getString(R.string.pluto_network___waiting_for_response),
                    context.color(R.color.pluto___text_dark_40)
                )
            )
        )
    )
}

internal fun tapIndicatorText(context: Context) = context.createSpan {
    append("\t\t")
    append(
        italic(
            light(
                fontColor(
                    context.getString(R.string.pluto_network___tap_for_details),
                    context.color(R.color.pluto___text_dark_40)
                )
            )
        )
    )
}

internal fun binaryBodyText(context: Context) = context.createSpan {
    append(
        italic(
            light(
                fontColor(
                    context.getString(R.string.pluto_network___binary_body),
                    context.color(R.color.pluto___text_dark_40)
                )
            )
        )
    )
}

internal fun getHeadersData(context: Context, headers: Map<String, String?>, onClick: () -> Unit) = KeyValuePairData(
    key = context.getString(R.string.pluto_network___headers_title),
    value = context.createSpan {
        if (headers.isNotEmpty()) {
            append(semiBold(context.resources.getQuantityString(R.plurals.pluto_network___headers_value_text, headers.size, headers.size)))
            append(tapIndicatorText(context))
        } else {
            append(fontColor("--", context.color(R.color.pluto___text_dark_40)))
        }
    },
    showClickIndicator = headers.isNotEmpty(),
    onClick = if (headers.isNotEmpty()) {
        { onClick.invoke() }
    } else null
)

@SuppressWarnings("UnnecessaryParentheses")
internal fun getBodyData(context: Context, body: ProcessedBody?, onClick: () -> Unit) = KeyValuePairData(
    key = context.getString(R.string.pluto_network___body_title),
    value = context.createSpan {
        body?.let {
            if (it.isBinary) {
                append(binaryBodyText(context))
            } else {
                if (it.sizeInBytes > 0) {
                    append(semiBold(formatSizeAsBytes(it.sizeInBytes)))
                    append(tapIndicatorText(context))
                } else {
                    append(
                        fontColor(
                            "0 bytes",
                            context.color(R.color.pluto___text_dark_40)
                        )
                    )
                }
            }
        } ?: run {
            append(fontColor("--", context.color(R.color.pluto___text_dark_40)))
        }
    },
    showClickIndicator = body?.isBinary != true && (body?.sizeInBytes ?: 0L) > 0,
    onClick = body?.let {
        if (it.isBinary) {
            null
        } else {
            if (it.sizeInBytes > 0) {
                { onClick.invoke() }
            } else {
                null
            }
        }
    }
)

internal fun getQueryParamsData(
    context: Context,
    url: String,
    onClick: () -> Unit
): KeyValuePairData {
    val url = url.asUrl()
    return KeyValuePairData(
        key = context.getString(R.string.pluto_network___query_params_title),
        value = context.createSpan {
            if (url.parameters.isEmpty()) {
                append(
                    semiBold(
                        context.resources.getQuantityString(
                            R.plurals.pluto_network___query_params_value_text,
                            url.querySize,
                            url.querySize
                        )
                    )
                )
                append(tapIndicatorText(context))
            } else {
                append(fontColor("--", context.color(R.color.pluto___text_dark_40)))
            }
        },
        showClickIndicator = url.querySize > 0,
        onClick = if (url.querySize > 0) {
            { onClick.invoke() }
        } else {
            null
        }
    )
}

private val Url.querySize: Int
    get() = parameters.names().size
