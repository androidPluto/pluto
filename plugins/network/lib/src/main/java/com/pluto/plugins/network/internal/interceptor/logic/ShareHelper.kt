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
    if (response != null) {
        text.append(
            "\n\nRequested at : ${response!!.sendTimeMillis.asFormattedDate("MMM dd, yyyy, HH:mm:ss.SSS")}"
        )
        text.append(
            "\nReceived at : ${response!!.receiveTimeMillis.asFormattedDate("MMM dd, yyyy, HH:mm:ss.SSS")}"
        )
        text.append("\nDelay : ${response!!.receiveTimeMillis - response!!.sendTimeMillis} ms")
        text.append("\nProtocol : ${response!!.protocol.name}")
    } else {
        text.append(
            "\n\nRequested at : ${request.timestamp.asFormattedDate("MMM dd, yyyy, HH:mm:ss.SSS")}"
        )
    }
    text.append("\n\n==================\n\n")
    text.append("REQUEST")
    text.append("\n\n*** Headers *** \n${moshiAdapter.toJson(request.headers)}")
    text.append("\n\n*** Body ***\n${request.body?.body}")

    response?.let { response ->
        text.append("\n\n==================\n\n")
        text.append("RESPONSE")
        text.append("\n\n*** Headers *** \n${response.headers}")
        text.append("\n\n*** Body *** \n${response.body?.body}")
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
    text.append("\n\n*** Headers *** \n${moshiAdapter.toJson(headers)}")
    text.append("\n\n*** Body ***\n${body?.body}")

    return text.toString()
}

internal fun ApiCallData.responseToText(): String {
    val text = StringBuilder()
    response?.let { response ->
        text.append("*** Headers *** \n${response.headers}")
        text.append("\n\n*** Body *** \n${response.body?.body}")
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
