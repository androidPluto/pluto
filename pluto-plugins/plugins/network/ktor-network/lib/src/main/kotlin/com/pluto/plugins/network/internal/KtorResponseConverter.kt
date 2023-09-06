package com.pluto.plugins.network.internal

import com.pluto.plugins.network.ProcessedBody
import com.pluto.plugins.network.ResponseData
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.contentType

internal object KtorResponseConverter : ResponseConverter<HttpResponse> {
    override suspend fun HttpResponse.convert(): ResponseData {
        return ResponseData(
            statusCode = status.value,
            body = extractBody(),
            protocol = version.name,
            fromDiskCache = false,
            headers = headersMap(headers),
            sentTimestamp = requestTime.timestamp,
            receiveTimestamp = responseTime.timestamp
        )
    }


//    private fun HttpResponse.statusCodeMessage(): String {
//        return status.description
//    }

    private fun headersMap(headers: Headers): Map<String, String?> {
        return headers.entries().associate {
            it.key to it.value.joinToString(",")
        }
    }

    // TODO handle gzip
    private suspend fun HttpResponse.extractBody() = ProcessedBody(
        body = bodyAsText(),
        contentType = contentType().toString()
    )

//    private fun HttpResponse.isTextType(): Boolean {
//        return contentType()?.run {
//            return when {
//                match(ContentType.Application.Json) -> true
//                match(ContentType.Application.Xml) -> true
//                match(ContentType.Application.FormUrlEncoded) -> true
//                match(ContentType.Text.Any) -> true
//                else -> false
//            }
//        } ?: false
//
//    }
//
//    private val HttpResponse.isGzipped: Boolean
//        get() = headers.contains("Content-Encoding", "gzip")


}