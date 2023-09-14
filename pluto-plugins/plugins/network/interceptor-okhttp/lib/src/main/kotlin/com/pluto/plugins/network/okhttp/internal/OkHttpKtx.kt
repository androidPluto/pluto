package com.pluto.plugins.network.okhttp.internal

import java.net.HttpURLConnection
import okhttp3.Request
import okhttp3.Response

private const val HTTP_CONTINUE = 100

internal fun Response.hasBody(): Boolean {
    if (request.method == "HEAD") {
        return false
    }
    body?.let {
        val responseCode = code
        val isSuccessResponse = responseCode < HTTP_CONTINUE || responseCode >= HttpURLConnection.HTTP_OK
        if (isSuccessResponse &&
            responseCode != HttpURLConnection.HTTP_NO_CONTENT &&
            responseCode != HttpURLConnection.HTTP_NOT_MODIFIED
        ) {
            return true
        }

        return contentLength > 0 || isChunked
    }
    return false
}

internal val Response.contentLength: Long
    get() {
        return this.header("Content-Length")?.toLongOrNull() ?: 0
    }

internal val Response.isChunked: Boolean
    get() {
        return this.header("Transfer-Encoding").equals("chunked", ignoreCase = true)
    }

internal val Response.contentType: String?
    get() {
        return this.header("Content-Type")
    }

internal val Response.isGzipped: Boolean
    get() {
        return this.header("Content-Encoding").equals("gzip", ignoreCase = true)
    }

internal val Request.isGzipped: Boolean
    get() {
        return this.header("Content-Encoding").equals("gzip", ignoreCase = true)
    }

