package com.mocklets.pluto.network.internal

import android.content.Context
import com.mocklets.pluto.network.R
import com.mocklets.pluto.network.internal.interceptor.doUnZipToString
import com.mocklets.pluto.network.internal.transformers.FormEncodedTransformer
import com.mocklets.pluto.network.internal.transformers.JsonBaseTransformer
import com.mocklets.pluto.network.internal.transformers.XmlBaseTransformer
import com.mocklets.pluto.utilities.DebugLog
import com.mocklets.pluto.utilities.extensions.color
import com.mocklets.pluto.utilities.spannable.createSpan
import java.nio.charset.Charset
import okhttp3.HttpUrl
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import okio.IOException

internal fun RequestBody.convertPretty(gzipped: Boolean): ProcessedBody {
    contentType()?.let {
        DebugLog.e(LOGTAG, "request : ${it.type()}, ${it.subtype()}, ${it.charset()}")
        return if (it.isText()) {
            val plainBody = convert(gzipped)
            ProcessedBody(
                isValid = true,
                body = it.beautify(plainBody),
                mediaType = it.type(),
                mediaSubtype = it.subtype()
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
    return ProcessedBody(
        isValid = false,
        mediaType = null,
        mediaSubtype = null
    )
}

internal fun ResponseBody?.convertPretty(buffer: Buffer): ProcessedBody? {
    this?.let {
        val contentType = it.contentType()
        if (contentType != null) {
            return if (contentType.isText()) {
                val body = buffer.readString(contentType.charset(UTF8) ?: UTF8)
                ProcessedBody(
                    isValid = true,
                    body = contentType.beautify(body),
                    mediaType = contentType.type(),
                    mediaSubtype = contentType.subtype()
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
        return ProcessedBody(
            isValid = false,
            mediaType = null,
            mediaSubtype = null
        )
    }
    return null
}

private fun RequestBody.convert(gzipped: Boolean): CharSequence {
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

internal fun Context?.beautifyHeaders(data: Map<String, String?>): CharSequence? {
    return this?.createSpan {
        data.forEach {
            append("${it.key} : ")
            if (it.value != null) {
                append(fontColor(semiBold("${it.value}"), context.color(R.color.pluto___text_dark_80)))
            } else {
                append(fontColor(light(italic("null")), context.color(R.color.pluto___text_dark_40)))
            }
            append("\n")
        }
    }?.trim()
}

internal fun Context?.beautifyQueryParams(url: HttpUrl): CharSequence? {
    return this?.createSpan {
        url.queryParameterNames().forEach {
            append("$it : ")
            val value = url.queryParameter(it)
            if (value != null) {
                append(fontColor(semiBold("$value"), context.color(R.color.pluto___text_dark_80)))
            } else {
                append(fontColor(light(italic("null")), context.color(R.color.pluto___text_dark_40)))
            }
            append("\n")
        }
    }?.trim()
}

internal fun ProcessedBody.flatten(): String? {
    body?.toString()?.let { body ->
        return when {
            mediaType == "binary" -> body
            mediaSubtype == "json" -> JsonBaseTransformer().flatten(body)
            mediaSubtype == "xml" || mediaSubtype == "html" -> XmlBaseTransformer().flatten(body)
            mediaSubtype == "x-www-form-urlencoded" -> FormEncodedTransformer().flatten(body)
            else -> body
        }
    }
    return null
}

internal fun String.pruneQueryParams(): String {
    val separated: List<String> = split("?")
    return separated[0]
}

internal fun HttpUrl.hostUrl(): String {
    val hostString = StringBuilder()
    hostString.append("${scheme()}://${host()}")
    if (port() != HTTP_PORT && port() != HTTPS_PORT) {
        hostString.append(":${port()}")
    }
    return hostString.toString()
}

internal const val LOGTAG = "pluto_sdk"
internal const val BODY_INDENTATION = 4
private const val BINARY_BODY = "~ Binary Data"
internal const val BINARY_MEDIA_TYPE = "binary"
internal val UTF8 = Charset.forName("UTF-8")
private const val HTTP_PORT = 80
private const val HTTPS_PORT = 443
