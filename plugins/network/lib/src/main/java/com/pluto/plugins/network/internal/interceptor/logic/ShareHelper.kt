package com.pluto.plugins.network.internal.interceptor.logic

import com.pluto.plugin.utilities.extensions.asFormattedDate
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@Suppress("StringLiteralDuplication")
internal fun ApiCallData.toShareText(): String {
    val moshi = Moshi.Builder().build()
    val moshiAdapter: JsonAdapter<Map<String, Any?>?> = moshi.adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))

    val text = StringBuilder()
    text.append("${request.method.uppercase()}, ${request.url}  ")
    text.append(
        "\n\nRequested at : ${request.timestamp.asFormattedDate("MMM dd, yyyy, HH:mm:ss.SSS")}"
    )
    response?.let {
        text.append(
            "\nReceived at : ${it.receiveTimeMillis.asFormattedDate("MMM dd, yyyy, HH:mm:ss.SSS")}"
        )
        text.append("\nDelay : ${it.receiveTimeMillis - it.sendTimeMillis} ms")
        text.append("\nProtocol : ${it.protocol.name}")
    }
    text.append("\n\n==================\n\n")
    text.append("REQUEST")
    text.append("\n\n*** Headers (${request.headers.size} items) *** \n${moshiAdapter.toJson(request.headers)}")
    text.append("\n\n*** Body (${formatSizeAsBytes(request.body?.sizeInBytes ?: 0L)}) ***\n${request.body?.body}")

    response?.let { response ->
        text.append("\n\n==================\n\n")
        text.append("RESPONSE")
        text.append("\n\n*** Headers (${response.headers.size} items) *** \n${moshiAdapter.toJson(response.headers)}")
        text.append("\n\n*** Body (${formatSizeAsBytes(response.body?.sizeInBytes ?: 0L)}) *** \n${response.body?.body}")
    }
    exception?.let { exception ->
        text.append("\n\n==================\n\n")
        text.append("RESPONSE\n")
        text.append("\n${exception.name}: ${exception.message}\n")
        exception.stackTrace.take(STACK_TRACE_LENGTH).forEach {
            text.append("\t at $it\n")
        }
        if (exception.stackTrace.size - STACK_TRACE_LENGTH > 0) {
            text.append("\t + ${exception.stackTrace.size - STACK_TRACE_LENGTH} more lines")
        }
    }
    return text.toString()
}

internal fun RequestData.toShareText(): String {
    val moshi = Moshi.Builder().build()
    val moshiAdapter: JsonAdapter<Map<String, Any?>?> = moshi.adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))

    val text = StringBuilder()
    text.append("${method.uppercase()}, $url")
    text.append("\n\n*** Headers (${headers.size} items) *** \n${moshiAdapter.toJson(headers)}")
    text.append("\n\n*** Body (${formatSizeAsBytes(body?.sizeInBytes ?: 0L)}) ***\n${body?.body}")

    return text.toString()
}

internal fun ApiCallData.responseToText(): String {
    val moshi = Moshi.Builder().build()
    val moshiAdapter: JsonAdapter<Map<String, Any?>?> = moshi.adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))

    val text = StringBuilder()
    response?.let { response ->
        text.append("*** Headers (${response.headers.size} items) *** \n${moshiAdapter.toJson(response.headers)}")
        text.append("\n\n*** Body (${formatSizeAsBytes(response.body?.sizeInBytes ?: 0L)}) *** \n${response.body?.body}")
    }
    exception?.let { exception ->
        text.append("*** Exception *** \n${exception.name}: ${exception.message}\n")
        exception.stackTrace.take(STACK_TRACE_LENGTH).forEach {
            text.append("\t at $it\n")
        }
        if (exception.stackTrace.size - STACK_TRACE_LENGTH > 0) {
            text.append("\t + ${exception.stackTrace.size - STACK_TRACE_LENGTH} more lines")
        }
    }
    return text.toString()
}

internal fun RequestData.getCurl(): String {
    val curlCommandBuilder = StringBuilder("")
    curlCommandBuilder.append("cURL")
    curlCommandBuilder.append(" -X")
    curlCommandBuilder.append(" ${method.uppercase()}")
    for (headerName in headers) {
        curlCommandBuilder.append(headerPair(headerName.key, headerName.value))
    }

    body?.let {
        curlCommandBuilder.append(" -d '${it.body}'")
    }
    curlCommandBuilder.append(" \"$url\"")
    curlCommandBuilder.append(" -L")
    return curlCommandBuilder.toString() // beautify(request.url.toString(), curlCommandBuilder.toString())
}

private fun headerPair(headerName: String, headerValue: String?): String {
    return " -H \"$headerName: $headerValue\""
}

private const val STACK_TRACE_LENGTH = 10
