package com.pluto.plugins.network.internal.interceptor.logic

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
