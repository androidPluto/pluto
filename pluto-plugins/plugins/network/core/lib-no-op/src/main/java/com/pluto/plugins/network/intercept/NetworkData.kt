package com.pluto.plugins.network.intercept

class NetworkData {

    data class Request(
        val url: String,
        val method: String,
        val body: Body?,
        val headers: Map<String, String?>,
        val sentTimestamp: Long
    )

    data class Response(
        val statusCode: Int,
        val body: Body?,
        val headers: Map<String, String?>,
        val sentTimestamp: Long,
        val receiveTimestamp: Long,
        val protocol: String = "",
        val fromDiskCache: Boolean = false
    ) {
        val isSuccessful: Boolean
            get() = statusCode in 200..299
    }

    data class Body(
        val body: CharSequence,
        val contentType: String
    ) {
        val sizeInBytes: Long = body.length.toLong()
    }
}
