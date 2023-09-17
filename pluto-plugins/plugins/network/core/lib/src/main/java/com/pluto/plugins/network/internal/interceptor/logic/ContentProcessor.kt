package com.pluto.plugins.network.internal.interceptor.logic

import android.content.Context
import com.pluto.plugins.network.R
import com.pluto.plugins.network.intercept.NetworkData.Body
import com.pluto.plugins.network.internal.interceptor.logic.transformers.FormEncodedTransformer
import com.pluto.plugins.network.internal.interceptor.logic.transformers.JsonTransformer
import com.pluto.plugins.network.internal.interceptor.logic.transformers.XmlTransformer
import com.pluto.utilities.extensions.color
import com.pluto.utilities.spannable.createSpan
import io.ktor.http.Url
import io.ktor.util.flattenForEach
import java.math.BigDecimal
import java.nio.charset.Charset

internal fun Context.beautifyHeaders(data: Map<String, String?>): CharSequence {
    return createSpan {
        data.forEach {
            append(fontColor("${it.key} : ", context.color(R.color.pluto___text_dark_40)))
            if (it.value != null) {
                append(fontColor("${it.value}", context.color(R.color.pluto___text_dark_80)))
            } else {
                append(
                    fontColor(
                        light(italic("null")),
                        context.color(R.color.pluto___text_dark_40)
                    )
                )
            }
            append("\n")
        }
    }.trim()
}

internal fun Context.beautifyQueryParams(url: String): CharSequence {
    val url = Url(url)
    return createSpan {
        url.parameters.flattenForEach { key, value ->
            append(fontColor("$key :", context.color(R.color.pluto___text_dark_40)))
            append(fontColor(value, context.color(R.color.pluto___text_dark_80)))
            append("\n")
        }
    }.trim()
}

// internal fun ProcessedBody.flatten(): String {
//    return body.toString().let { body ->
//        when {
//            mediaType == "binary" -> body
//            mediaSubtype == "json" -> JsonTransformer().flatten(body)
//            mediaSubtype == "xml" || mediaSubtype == "html" -> XmlTransformer().flatten(body)
//            mediaSubtype == "x-www-form-urlencoded" -> FormEncodedTransformer().flatten(body)
//            else -> body
//        }
//    }
// }

internal fun Body.beautify(): CharSequence {
    return when {
        mediaSubtype.endsWith("json") -> JsonTransformer().beautify(body)
        mediaSubtype == "xml" || mediaSubtype == "html" -> XmlTransformer().beautify(body)
        mediaSubtype == "x-www-form-urlencoded" -> FormEncodedTransformer().beautify(body)
        else -> body
    }
}

internal fun String.pruneQueryParams(): String = split("?")[0]

internal fun formatSizeAsBytes(origin: Long): String {
    var size = BigDecimal(origin.toString())
    return if (size < KILO_BYTES) {
        "$size bytes"
    } else {
        size = size.divide(KILO_BYTES)
        if (size > KILO_BYTES) {
            "${size.divide(KILO_BYTES, 2, BigDecimal.ROUND_DOWN)} MB"
        } else {
            "${size.setScale(2, BigDecimal.ROUND_DOWN)} KB"
        }
    }
}

internal const val LOGTAG = "pluto_network"

val UTF8: Charset = Charset.forName("UTF-8")
private val KILO_BYTES = BigDecimal("1024")
internal const val MAX_BLOB_LENGTH = 25_000
internal val RESPONSE_ERROR_STATUS_RANGE = 400..499
