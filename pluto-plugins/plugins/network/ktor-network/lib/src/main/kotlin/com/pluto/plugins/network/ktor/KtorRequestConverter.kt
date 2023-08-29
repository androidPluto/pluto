package com.pluto.plugins.network.ktor

import com.pluto.plugins.network.base.internal.interceptor.logic.ProcessedBody
import com.pluto.plugins.network.base.internal.interceptor.logic.RequestData
import com.pluto.plugins.network.base.internal.interceptor.logic.core.RequestConverter
import io.ktor.client.request.HttpRequestData
import io.ktor.http.content.OutgoingContent

internal object KtorRequestConverter : RequestConverter<HttpRequestData> {
    override fun HttpRequestData.convert(): RequestData {
        return RequestData(
            url.toString(),
            method.value,
            processBody(),
            headerMap(),
            System.currentTimeMillis(),
            isGzipped
        )
    }

    private fun HttpRequestData.headerMap(): Map<String, String?> {
        return headers.entries()
            .associate {
                it.key to it.value.joinToString(",")
            }
    }

    private val HttpRequestData.isGzipped: Boolean
        get() = headers.contains("content-Encoding", "gzip")

    private fun HttpRequestData.processBody(): ProcessedBody {
        return ProcessedBody(
            isValid = true,
            body = extractBody(isGzipped),
            mediaType = body.contentType.toString(),
            mediaSubtype = body.contentType?.contentSubtype.toString()
        )
    }

    // TODO handle gzip
    private fun HttpRequestData.extractBody(isGzipped: Boolean): CharSequence {
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