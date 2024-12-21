package com.pluto.plugins.network.intercept

import com.pluto.plugins.network.internal.Status
import com.pluto.plugins.network.internal.interceptor.logic.mapCode2Message
import io.ktor.http.ContentType
import org.json.JSONObject

class NetworkData {

    data class Request(
        val url: String,
        val method: String,
        val body: Body?,
        val headers: Map<String, String?>,
        val sentTimestamp: Long,
    ) {
        data class GraphqlData(
            val queryType: String,
            val queryName: String,
            val variables: JSONObject,
        )

        val graphqlData: GraphqlData? = parseGraphqlData()

        private fun parseGraphqlData(): GraphqlData? {
            if (method != "POST" ||
                body == null ||
                !body.isJson
            ) return null
            val json = runCatching { JSONObject(body!!.body.toString()) }.getOrNull() ?: return null
            val query = json.optString("query") ?: return null
            val variables = json.optJSONObject("variables") ?: JSONObject()
            val match = graqphlQueryRegex.find(query)?.groupValues ?: return null
            return GraphqlData(
                queryType = match[1],
                queryName = match[2],
                variables = variables,
            )
        }

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
        val contentType: String,
    ) {
        private val contentTypeInternal: ContentType = ContentType.parse(contentType)
        private val mediaType: String = contentTypeInternal.contentType
        internal val mediaSubtype: String = contentTypeInternal.contentSubtype
        internal val isBinary: Boolean = BINARY_MEDIA_TYPES.contains(mediaType)
        val sizeInBytes: Long = body.length.toLong()
        internal val mediaTypeFull: String = "$mediaType/$mediaSubtype"
        val isJson get() = mediaTypeFull == "application/json"
    }

    companion object {
        internal val BINARY_MEDIA_TYPES = listOf("audio", "video", "image", "font")
        private val graqphlQueryRegex = Regex("""\b(query|mutation)\s+(\w+)""")
    }
}
