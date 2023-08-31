package com.pluto.plugins.network

import com.pluto.plugins.network.internal.KtorRequestConverter.convert
import com.pluto.plugins.network.internal.KtorResponseConverter.convert
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.url
import io.ktor.utils.io.errors.IOException

fun HttpClient.addPlutoKtorPlugin() {
    plugin(HttpSend).intercept { requestUnBuilt ->
        val request = requestUnBuilt.build()
        val networkRecorder = NetworkRecorder(request.convert())
        val callResult = try {
            requestUnBuilt.url(networkRecorder.requestUrlWithMockInfo)
            execute(requestUnBuilt)
        } catch (e: IOException) {
            networkRecorder.onError(e)
            throw e
        }
        networkRecorder.onResponse(callResult.response.convert())
        callResult
    }
}

