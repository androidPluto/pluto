package com.mocklets.pluto.modules.network

internal class CurlBuilder {

    fun get(request: RequestData): String {
        val curlCommandBuilder = StringBuilder("")
        curlCommandBuilder.append("cURL")
        curlCommandBuilder.append(" -X")
        curlCommandBuilder.append(" ${request.method.uppercase()}")
        for (headerName in request.headers) {
            curlCommandBuilder.append(headerPair(headerName.key, headerName.value))
        }

        request.body?.let {
            curlCommandBuilder.append(" -d '${it.flatten()}'")
        }
        curlCommandBuilder.append(" \"" + request.url.toString() + "\"")
        curlCommandBuilder.append(" -L")
        return curlCommandBuilder.toString() // beautify(request.url.toString(), curlCommandBuilder.toString())
    }

    private fun headerPair(headerName: String, headerValue: String?): String {
        return " -H \"$headerName: $headerValue\""
    }

//    private fun beautify(url: String, curl: String): String {
//        val logMsg = java.lang.StringBuilder("\n")
//        logMsg.append(curl)
//        logMsg.append(" ")
//        return logMsg.toString()
//    }
}
