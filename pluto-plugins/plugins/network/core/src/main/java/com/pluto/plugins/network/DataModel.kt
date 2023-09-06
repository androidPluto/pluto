package com.pluto.plugins.network

import com.pluto.plugins.network.internal.interceptor.logic.ExceptionData
import com.pluto.plugins.network.internal.interceptor.logic.mapCode2Message
import com.pluto.plugins.network.internal.share.getCurl
import com.pluto.utilities.list.ListItem
import io.ktor.http.ContentType

data class RequestData(
    val url: String,
    val method: String,
    val body: ProcessedBody?,
    val headers: Map<String, String?>,
    val sentTimestamp: Long
) {
    internal val isGzipped: Boolean
        get() = headers["Content-Encoding"].equals("gzip", ignoreCase = true)
}

data class ResponseData(
    val statusCode: Int,
    val body: ProcessedBody?,
    val headers: Map<String, String?>,
    val sentTimestamp: Long,
    val receiveTimestamp: Long,
    val protocol: String = "",
    val fromDiskCache: Boolean = false
) {
    internal val status: Status
        get() = Status(statusCode, mapCode2Message(statusCode))
    val isSuccessful: Boolean
        get() = statusCode in 200..299
    internal val isGzipped: Boolean
        get() = headers["Content-Encoding"].equals("gzip", ignoreCase = true)
}

internal data class MockConfig(
    val url: String,
)

internal data class Status(
    val code: Int,
    val message: String,
)

internal class ApiCallData(
    val id: String,
    val request: RequestData,
    var response: ResponseData? = null,
    var exception: ExceptionData? = null,
    var mock: MockConfig? = null,
    val isCustomTrace: Boolean = false
) : ListItem() {
    val curl: String
        get() = request.getCurl()
    val responseTime
        get() = exception?.timeStamp ?: response?.receiveTimestamp

    override fun isEqual(other: Any): Boolean {
        if (other is ApiCallData) {
            id == other.id && response == other.response && exception == other.exception
        }
        return false
    }
}

data class ProcessedBody(
    val body: CharSequence,
    val contentType: String
) {
    private val contentTypeInternal: ContentType = ContentType.parse(contentType)
    private val mediaType: String = contentTypeInternal.contentType
    internal val mediaSubtype: String = contentTypeInternal.contentSubtype
    val isBinary: Boolean = BINARY_MEDIA_TYPES.contains(mediaType)
    val sizeInBytes: Long = body.length.toLong()
    val mediaTypeFull: String = "$mediaType/$mediaSubtype"
}

val BINARY_MEDIA_TYPES = listOf("audio", "video", "image", "font")
