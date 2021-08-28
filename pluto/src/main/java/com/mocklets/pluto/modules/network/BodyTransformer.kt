package com.mocklets.pluto.modules.network

import android.content.Context
import com.mocklets.pluto.R
import com.mocklets.pluto.core.DebugLog
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.ui.spannable.createSpan
import com.mocklets.pluto.modules.network.interceptor.doUnZipToString
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
                body = it.beautify(plainBody)
            )
        } else {
            ProcessedBody(
                isValid = true,
                body = BINARY_BODY,
                isBinary = true
            )
        }
    }
    return ProcessedBody(
        isValid = false
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
                    body = contentType.beautify(body)
                )
            } else {
                // todo process image response
                ProcessedBody(
                    isValid = true,
                    body = BINARY_BODY,
                    isBinary = true
                )
            }
        }
        return ProcessedBody(
            isValid = false
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

internal fun ProcessedBody?.flatten(): String? {
    this?.let {
        it.body?.toString()?.let { body ->
            return if (it.isBinary) {
                body
            } else {
                body.replace("\n", "").replace("\\s+".toRegex(), "")
            }
        }
        return null
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
internal val UTF8 = Charset.forName("UTF-8")
private const val HTTP_PORT = 80
private const val HTTPS_PORT = 443
