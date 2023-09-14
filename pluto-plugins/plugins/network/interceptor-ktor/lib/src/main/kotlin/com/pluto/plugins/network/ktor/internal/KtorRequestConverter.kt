package com.pluto.plugins.network.ktor.internal

import com.pluto.plugins.network.intercept.NetworkData.Body
import com.pluto.plugins.network.intercept.NetworkData.Request
import io.ktor.client.request.HttpRequestData
import io.ktor.http.Headers
import io.ktor.http.content.OutgoingContent

internal object KtorRequestConverter : RequestConverter<HttpRequestData> {
    override fun HttpRequestData.convert(): Request {
        return Request(
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

    private fun processBody(body: OutgoingContent, isGzipped: Boolean): Body? {
        return body.contentType?.let {
            Body(
                body = extractBody(body, isGzipped),
                contentType = it.toString()
            )
        } ?: run {
            null
        }
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