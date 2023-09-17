package com.pluto.plugins.network.intercept

import com.pluto.plugins.network.internal.Status
import com.pluto.plugins.network.internal.interceptor.logic.mapCode2Message
import io.ktor.http.ContentType

class NetworkData {

    data class Request(
        val url: String,
        val method: String,
        val body: Body?,
        val headers: Map<String, String?>,
        val sentTimestamp: Long
    ) {
        internal val isGzipped: Boolean
            get() = headers["Content-Encoding"].equals("gzip", ignoreCase = true)
    }

    data class Response(
        private val statusCode: Int,
        val body: Body?,
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

    data class Body(
        val body: CharSequence,
        val contentType: String
    ) {
        private val contentTypeInternal: ContentType = ContentType.parse(contentType)
        private val mediaType: String = contentTypeInternal.contentType
        internal val mediaSubtype: String = contentTypeInternal.contentSubtype
        internal val isBinary: Boolean = BINARY_MEDIA_TYPES.contains(mediaType)
        val sizeInBytes: Long = body.length.toLong()
        internal val mediaTypeFull: String = "$mediaType/$mediaSubtype"
    }

    companion object {
        internal val BINARY_MEDIA_TYPES = listOf("audio", "video", "image", "font")
    }
}
