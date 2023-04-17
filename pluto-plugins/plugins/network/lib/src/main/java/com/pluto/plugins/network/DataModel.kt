package com.pluto.plugins.network

import com.pluto.plugins.network.internal.interceptor.logic.ProcessedBody
import com.pluto.plugins.network.internal.interceptor.logic.RequestData
import com.pluto.plugins.network.internal.interceptor.logic.ResponseData
import com.pluto.plugins.network.internal.interceptor.logic.Status
import com.pluto.plugins.network.internal.interceptor.logic.core.mapCode2Message
import okhttp3.HttpUrl.Companion.toHttpUrl

data class CustomRequest(
    val url: String,
    val method: String,
    val body: CustomBody?,
    val headers: Map<String, String?>,
    val timestamp: Long,
    val isGzipped: Boolean
) {
    internal fun toRequestData(): RequestData = RequestData(
        url = url.toHttpUrl(),
        method = method,
        body = body?.toProcessBody(),
        headers = headers,
        timestamp = timestamp,
        isGzipped = isGzipped
    )
}

data class CustomResponse(
    val statusCode: Int,
    val body: CustomBody?,
    val headers: Map<String, String?>,
    val sendTimeMillis: Long,
    val receiveTimeMillis: Long,
    val isGzipped: Boolean
) {
    @SuppressWarnings("MagicNumber")
    internal fun toResponseData(): ResponseData = ResponseData(
        status = Status(statusCode, mapCode2Message(statusCode)),
        isSuccessful = statusCode in 200..299,
        body = body?.toProcessBody(),
        headers = headers,
        sendTimeMillis = sendTimeMillis,
        receiveTimeMillis = receiveTimeMillis,
        protocol = null,
        fromDiskCache = false,
        isGzipped = isGzipped
    )
}

data class CustomBody(
    val body: CharSequence,
    val mediaType: String,
    val mediaSubtype: String
) {
    internal fun toProcessBody(): ProcessedBody = ProcessedBody(
        isValid = true,
        body = body,
        mediaType = mediaType,
        mediaSubtype = mediaSubtype
    )
}
