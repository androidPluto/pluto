package com.pluto.plugins.network

import com.pluto.plugins.network.internal.interceptor.logic.BINARY_MEDIA_TYPE
import com.pluto.plugins.network.internal.interceptor.logic.ProcessedBody
import com.pluto.plugins.network.internal.interceptor.logic.UTF8
import com.pluto.utilities.DebugLog
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

internal fun MediaType.isText(): Boolean = (type == "application" || type == "text") &&
        (subtype.endsWith("json") || subtype == "plain" || subtype == "xml" || subtype == "html" || subtype == "x-www-form-urlencoded")

internal fun HttpUrl.hostUrl(): String = StringBuilder().apply {
    append("$scheme://$host")
    if (port != HTTP_PORT && port != HTTPS_PORT) {
        append(":$port")
    }
}.toString()

internal const val BODY_INDENTATION = 2
private const val BINARY_BODY = "~ Binary Data"
private const val HTTP_PORT = 80
private const val HTTPS_PORT = 443
private const val LOGTAG = "content-processor"