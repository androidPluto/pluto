package com.pluto.plugins.network.internal

import com.pluto.plugins.network.internal.interceptor.logic.BINARY_MEDIA_TYPE
import com.pluto.plugins.network.internal.interceptor.logic.ProcessedBody
import com.pluto.plugins.network.internal.interceptor.logic.ResponseData
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

internal object KtorResponseConverter : ResponseConverter<HttpResponse> {
    override suspend fun HttpResponse.convert(): ResponseData {
        return ResponseData(
            statusCode = status.value,
            isSuccessful = status.isSuccess(),
            body = extractBody(),
            protocol = version.name,
            fromDiskCache = false,
            headers = headersMap(),
            sendTimeMillis = requestTime.timestamp,
            receiveTimeMillis = responseTime.timestamp,
            isGzipped = isGzipped
        )
    }


    private fun HttpResponse.statusCodeMessage(): String {
        return status.description
    }

    private fun HttpResponse.headersMap(): Map<String, String?> {
        return headers.entries().associate {
            it.key to it.value.joinToString(",")
        }
    }

    // TODO handle gzip
    private suspend fun HttpResponse.extractBody(): ProcessedBody {
        val contentType = contentType()
        return if (isTextType()) {
            ProcessedBody(
                isValid = true,
                body = bodyAsText(),
                mediaType = contentType?.contentType.orEmpty(),
                mediaSubtype = contentType?.contentSubtype.orEmpty()
            )
        } else {
            ProcessedBody(
                isValid = true,
                body = "~binary",
                mediaType = BINARY_MEDIA_TYPE,
                mediaSubtype = BINARY_MEDIA_TYPE
            )
        }
    }

    private fun HttpResponse.isTextType(): Boolean {
        return contentType()?.run {
            return when {
                match(ContentType.Application.Json) -> true
                match(ContentType.Application.Xml) -> true
                match(ContentType.Application.FormUrlEncoded) -> true
                match(ContentType.Text.Any) -> true
                else -> false
            }
        } ?: false

    }

    private val HttpResponse.isGzipped: Boolean
        get() = headers.contains("Content-Encoding", "gzip")


}