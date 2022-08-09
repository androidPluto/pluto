package com.pluto.plugins.network.internal.interceptor.logic

import android.content.Context
import com.pluto.plugin.utilities.DebugLog
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.spannable.createSpan
import com.pluto.plugins.network.R
import com.pluto.plugins.network.internal.interceptor.logic.core.doUnZipToString
import com.pluto.plugins.network.internal.interceptor.logic.transformers.FormEncodedTransformer
import com.pluto.plugins.network.internal.interceptor.logic.transformers.JsonTransformer
import com.pluto.plugins.network.internal.interceptor.logic.transformers.XmlTransformer
import java.math.BigDecimal
import java.nio.charset.Charset
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import okio.IOException

internal fun RequestBody.processBody(gzipped: Boolean): ProcessedBody? {
    return contentType()?.let {
        DebugLog.e(LOGTAG, "request : ${it.type}, ${it.subtype}, ${it.charset()}")
        if (it.isText()) {
            val plainBody = extractBody(gzipped)
            ProcessedBody(
                isValid = true,
                body = plainBody,
                mediaType = it.type,
                mediaSubtype = it.subtype
            )
        } else {
            ProcessedBody(
                isValid = true,
                body = BINARY_BODY,
                mediaType = BINARY_MEDIA_TYPE,
                mediaSubtype = BINARY_MEDIA_TYPE
            )
        }
    }
}

internal fun ResponseBody?.processBody(buffer: Buffer): ProcessedBody? {
    return this?.contentType()?.let {
        DebugLog.e(LOGTAG, "response  : ${it.type}, ${it.subtype}, ${it.charset()}")
        if (it.isText()) {
            val body = buffer.readString(it.charset(UTF8) ?: UTF8)
            ProcessedBody(
                isValid = true,
                body = body,
                mediaType = it.type,
                mediaSubtype = it.subtype
            )
        } else {
            // todo process image response
            ProcessedBody(
                isValid = true,
                body = BINARY_BODY,
                mediaType = BINARY_MEDIA_TYPE,
                mediaSubtype = BINARY_MEDIA_TYPE
            )
        }
    }
}

private fun RequestBody.extractBody(gzipped: Boolean): CharSequence {
    return try {
        val buffer = Buffer()
        writeTo(buffer)
        if (gzipped) {
            doUnZipToString(buffer.readByteArray())
        } else {
            buffer.readUtf8()
        }
    } catch (e: IOException) {
        DebugLog.e(LOGTAG, "request body parsing failed", e)
        ""
    }
}

internal fun Context.beautifyHeaders(data: Map<String, String?>): CharSequence {
    return createSpan {
        data.forEach {
            append(fontColor("${it.key} : ", context.color(R.color.pluto___text_dark_40)))
            if (it.value != null) {
                append(fontColor("${it.value}", context.color(R.color.pluto___text_dark_80)))
            } else {
                append(fontColor(light(italic("null")), context.color(R.color.pluto___text_dark_40)))
            }
            append("\n")
        }
    }.trim()
}

internal fun Context.beautifyQueryParams(url: HttpUrl): CharSequence {
    return createSpan {
        url.queryParameterNames.forEach {
            append(fontColor("$it : ", context.color(R.color.pluto___text_dark_40)))
            val value = url.queryParameter(it)
            if (value != null) {
                append(fontColor("$value", context.color(R.color.pluto___text_dark_80)))
            } else {
                append(fontColor(light(italic("null")), context.color(R.color.pluto___text_dark_40)))
            }
            append("\n")
        }
    }.trim()
}

//
// internal fun ProcessedBody.flatten(): String {
//    return body.toString().let { body ->
//        when {
//            mediaType == "binary" -> body
//            mediaSubtype == "json" -> JsonBaseTransformer().flatten(body)
//            mediaSubtype == "xml" || mediaSubtype == "html" -> XmlBaseTransformer().flatten(body)
//            mediaSubtype == "x-www-form-urlencoded" -> FormEncodedTransformer().flatten(body)
//            else -> body
//        }
//    }
// }
//
internal fun ProcessedBody.beautify(): CharSequence {
    return when {
        mediaSubtype.endsWith("json") -> JsonTransformer().beautify(body)
        mediaSubtype == "xml" || mediaSubtype == "html" -> XmlTransformer().beautify(body)
        mediaSubtype == "x-www-form-urlencoded" -> FormEncodedTransformer().beautify(body)
        else -> body
    }
}

internal fun MediaType.isText(): Boolean = (type == "application" || type == "text") &&
    (subtype.endsWith("json") || subtype == "plain" || subtype == "xml" || subtype == "html" || subtype == "x-www-form-urlencoded")

internal fun String.pruneQueryParams(): String = split("?")[0]

internal fun HttpUrl.hostUrl(): String = StringBuilder().apply {
    append("$scheme://$host")
    if (port != HTTP_PORT && port != HTTPS_PORT) {
        append(":$port")
    }
}.toString()

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
internal const val BODY_INDENTATION = 2
private const val BINARY_BODY = "~ Binary Data"
internal const val BINARY_MEDIA_TYPE = "binary"
internal val UTF8 = Charset.forName("UTF-8")
private const val HTTP_PORT = 80
private const val HTTPS_PORT = 443
private val KILO_BYTES = BigDecimal("1024")
internal const val MAX_BLOB_LENGTH = 25_000
