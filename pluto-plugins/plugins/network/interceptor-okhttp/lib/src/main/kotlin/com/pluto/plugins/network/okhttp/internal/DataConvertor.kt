package com.pluto.plugins.network.okhttp.internal

import com.pluto.plugins.network.intercept.NetworkData
import okhttp3.Request
import okhttp3.Response

internal fun Request.convert(): NetworkData.Request {
    val body = this.body?.processBody(this.isGzipped)
    return NetworkData.Request(
        url = this.url.toString(),
        method = this.method,
        body = body,
        headers = this.headerMap(body?.sizeInBytes ?: 0L),
        sentTimestamp = System.currentTimeMillis()
    )
}

internal fun Request.headerMap(contentLength: Long): Map<String, String?> {
    val headerNames = arrayListOf<String>()
    headerNames.addAll(headers.names())
    headerNames.add("content-type")
    headerNames.add("content-length")
    headerNames.sortBy { it }

    val map = mutableMapOf<String, String?>()
    headerNames.forEach {
        val key = it.lowercase().trim()
        when (it) {
            "content-type" -> body?.contentType()?.toString()?.let { value ->
                map[key] = value.trim()
            }
            "content-length" -> map[key] = headers[it]?.trim() ?: run { contentLength.toString() }
            else -> map[key] = headers[it]?.trim()
        }
    }
    return map
}

internal fun Response.convert(body: NetworkData.Body?): NetworkData.Response {
    return NetworkData.Response(
        statusCode = code,
        body = body,
        protocol = protocol.name,
        fromDiskCache = false,
        headers = headersMap(),
        sentTimestamp = sentRequestAtMillis,
        receiveTimestamp = receivedResponseAtMillis
    )
}

private fun Response.headersMap(): Map<String, String?> {
    val headerNames = arrayListOf<String>()
    headerNames.addAll(headers.names())
    headerNames.sortBy { it }

    val map = mutableMapOf<String, String?>()
    headerNames.forEach {
        map[it.lowercase().trim()] = headers[it]?.trim()
    }

    return map
}
