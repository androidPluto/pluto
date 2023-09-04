package com.pluto.plugins.network.internal

import com.pluto.plugins.network.ProcessedBody
import com.pluto.plugins.network.RequestData
import io.ktor.client.request.HttpRequestData
import io.ktor.http.Headers
import io.ktor.http.content.OutgoingContent

internal object KtorRequestConverter : RequestConverter<HttpRequestData> {
    override fun HttpRequestData.convert(): RequestData {
        return RequestData(
            url.toString(),
            method.value,
            processBody(body, isGzipped),
            headerMap(headers),
            System.currentTimeMillis()
        )
    }

    private fun headerMap(headers: Headers): Map<String, String?> {
        return headers.entries().associate {
            it.key to it.value.joinToString(",")
        }
    }

    private val HttpRequestData.isGzipped: Boolean
        get() = headers.contains("content-Encoding", "gzip")

    private fun processBody(body: OutgoingContent, isGzipped: Boolean): ProcessedBody {
        return ProcessedBody(
            body = extractBody(body, isGzipped),
            mediaType = body.contentType.toString(),
            mediaSubtype = body.contentType?.contentSubtype.toString()
        )
    }

    // TODO handle gzip
    private fun extractBody(body: OutgoingContent, isGzipped: Boolean): CharSequence {
        return body.run {
            when (this) {
                is OutgoingContent.ByteArrayContent -> {
                    (this).bytes().decodeToString()
                }

                is OutgoingContent.NoContent -> ""
                is OutgoingContent.ProtocolUpgrade -> ""
                is OutgoingContent.ReadChannelContent -> {
                    "Binary_Body"
                }

                is OutgoingContent.WriteChannelContent -> {
                    ""
                }
            }
        }
    }

}