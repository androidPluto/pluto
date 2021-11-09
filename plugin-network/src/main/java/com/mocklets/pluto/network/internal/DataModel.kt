package com.mocklets.pluto.network.internal

import androidx.annotation.Keep
import com.mocklets.pluto.utilities.list.ListItem
import okhttp3.HttpUrl
import okhttp3.Protocol

internal data class RequestData(
    val url: HttpUrl,
    val method: String,
    val body: ProcessedBody?,
    val headers: Map<String, String?>,
//    val headerCount: Int,
    val timestamp: Long,
    val isGzipped: Boolean,
)

internal data class ProxyConfig(
    val url: String,
)

internal data class ResponseData(
    val status: Status,
    val isSuccessful: Boolean,
    val body: ProcessedBody?,
    val protocol: Protocol,
    val fromDiskCache: Boolean,
    val headers: Map<String, String?>,
    val sendTimeMillis: Long,
    val receiveTimeMillis: Long,
    val isGzipped: Boolean,
)

internal data class Status(
    val code: Int,
    val message: String,
)

internal class ApiCallData(
    val id: String,
    val request: RequestData,
    var hasResponseBody: Boolean = false,
    var response: ResponseData? = null,
    var exception: ExceptionData? = null,
    var proxy: ProxyConfig? = null,
) : ListItem() {
    val curl: String = request.getCurl()
    override fun isEqual(other: Any): Boolean {
        if (other is ApiCallData) {
            id == other.id && response == other.response && exception == other.exception
        }
        return false
    }
}

internal data class ProcessedBody(
    val isValid: Boolean,
    val body: CharSequence? = null,
    val mediaType: String?,
    val mediaSubtype: String?,
) {
    val isBinary: Boolean = mediaType == BINARY_MEDIA_TYPE
}

@Keep
internal data class ExceptionData(
    val message: String?,
    val name: String?,
    val file: String?,
    val lineNumber: Int,
    val stackTrace: ArrayList<String>,
    val timeStamp: Long = System.currentTimeMillis(),
    val isANRException: Boolean = false,
) : ListItem()
