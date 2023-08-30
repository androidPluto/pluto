package com.pluto.plugins.network.internal.share

import com.pluto.plugins.network.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.internal.interceptor.logic.RequestData
import com.pluto.plugins.network.internal.interceptor.logic.formatSizeAsBytes
import com.pluto.utilities.extensions.asFormattedDate

@Suppress("StringLiteralDuplication")
internal fun ApiCallData.toShareText(): String {
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
        text.append("\nProtocol : ${it.protocol}")
    }
    text.append("\n\n==================\n\n")
    text.append("REQUEST")
    text.append("\n\n*** Headers (${request.headers.size} items) *** \n${request.headers.toShareText()}")
    text.append("\n*** Body (${formatSizeAsBytes(request.body?.sizeInBytes ?: 0L)}) ***\n${request.body?.body}")

    response?.let { response ->
        text.append("\n\n==================\n\n")
        text.append("RESPONSE")
        text.append("\n\n*** Headers (${response.headers.size} items) *** \n${response.headers.toShareText()}")
        text.append("\n*** Body (${formatSizeAsBytes(response.body?.sizeInBytes ?: 0L)}) *** \n${response.body?.body}")
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
    val text = StringBuilder()
    text.append("${method.uppercase()}, $url")
    text.append("\n\n*** Headers (${headers.size} items) *** \n${headers.toShareText()}")
    text.append("\n*** Body (${formatSizeAsBytes(body?.sizeInBytes ?: 0L)}) ***\n${body?.body}")

    return text.toString()
}

internal fun ApiCallData.responseToText(): String {
    val text = StringBuilder()
    response?.let { response ->
        text.append("*** Headers (${response.headers.size} items) *** \n${response.headers.toShareText()}")
        text.append("\n*** Body (${formatSizeAsBytes(response.body?.sizeInBytes ?: 0L)}) *** \n${response.body?.body}")
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

private fun Map<String, String?>.toShareText(): String {
    val text = StringBuilder()
    forEach { entry ->
        text.append("${entry.key} : ${entry.value}\n")
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
