package com.pluto.plugins.network

data class RequestData(
    val url: String,
    val method: String,
    val body: ProcessedBody?,
    val headers: Map<String, String?>,
    val sentTimestamp: Long
)

data class ResponseData(
    val statusCode: Int,
    val body: ProcessedBody?,
    val headers: Map<String, String?>,
    val sentTimestamp: Long,
    val receiveTimestamp: Long,
    val protocol: String = "",
    val fromDiskCache: Boolean = false
)

data class ProcessedBody(
    val body: CharSequence,
    val contentType: String
)
