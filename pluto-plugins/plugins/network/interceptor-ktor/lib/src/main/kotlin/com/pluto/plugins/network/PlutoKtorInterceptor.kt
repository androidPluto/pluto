package com.pluto.plugins.network

import com.pluto.plugins.network.intercept.NetworkInterceptor
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
        val networkInterceptor = NetworkInterceptor.intercept(request.convert(), NetworkInterceptor.Option(NAME))
        val callResult = try {
            requestUnBuilt.url(networkInterceptor.requestUrlWithMockInfo)
            execute(requestUnBuilt)
        } catch (e: IOException) {
            networkInterceptor.onError(e)
            throw e
        }
        networkInterceptor.onResponse(callResult.response.convert())
        callResult
    }
}

private const val NAME = "Ktor"

