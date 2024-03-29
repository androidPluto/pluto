package com.pluto.plugins.network.okhttp.internal

import com.pluto.plugins.network.intercept.NetworkData.Body
import com.pluto.plugins.network.internal.interceptor.logic.UTF8
import com.pluto.utilities.DebugLog
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import okio.IOException

internal fun RequestBody.processBody(gzipped: Boolean): Body? {
    return contentType()?.let {
        DebugLog.e(LOG_TAG, "request : ${it.type}, ${it.subtype}, ${it.charset()}")
        Body(
            body = if (it.isText()) extractBody(gzipped) else BINARY_BODY,
            contentType = it.toString()
        )
    }
}

internal fun ResponseBody?.processBody(buffer: Buffer): Body? {
    return this?.contentType()?.let {
        DebugLog.e(LOG_TAG, "response  : ${it.type}, ${it.subtype}, ${it.charset()}")
        Body(
            body = if (it.isText()) buffer.readString(it.charset(UTF8) ?: UTF8) else BINARY_BODY,
            contentType = it.toString()
        )
    }
}

private fun RequestBody.extractBody(gzipped: Boolean): CharSequence {
    return try {
        val buffer = Buffer()
        writeTo(buffer)
        if (gzipped) {
            buffer.readByteArray().unzipToString()
        } else {
            buffer.readUtf8()
        }
    } catch (e: IOException) {
        DebugLog.e(LOG_TAG, "request body parsing failed", e)
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
private const val LOG_TAG = "content-processor"