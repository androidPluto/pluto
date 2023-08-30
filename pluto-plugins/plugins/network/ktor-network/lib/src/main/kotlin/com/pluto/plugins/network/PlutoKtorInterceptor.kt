package com.pluto.plugins.network

import com.pluto.plugins.network.internal.interceptor.logic.MockConfig
import com.pluto.plugins.network.internal.interceptor.logic.NetworkCallsRepo
import com.pluto.plugins.network.internal.interceptor.logic.asExceptionData
import com.pluto.plugins.network.internal.mock.logic.MockSettingsRepo
import com.pluto.plugins.network.internal.KtorRequestConverter.save
import com.pluto.plugins.network.internal.KtorResponseConverter.save
import com.pluto.utilities.DebugLog
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.url
import io.ktor.utils.io.errors.IOException
import java.util.UUID

fun HttpClient.addPlutoKtorPlugin() {
    plugin(HttpSend).intercept { requestUnBuilt ->
        val request = requestUnBuilt.build()
        val id =
            UUID.nameUUIDFromBytes("${System.currentTimeMillis()}::${request.url}".toByteArray())
                .toString()
        DebugLog.d("interceptor : ot", "$id ${request.url}")
        val apiCallData = request.save()
        val mockUrl = MockSettingsRepo.get(
            url = request.url.toString(),
            method = request.method.value
        )
        mockUrl?.let {
            requestUnBuilt.url(mockUrl)
            apiCallData.mock = MockConfig(mockUrl)
            NetworkCallsRepo.set(apiCallData)
            return@intercept execute(requestUnBuilt)
        }
        val callResult =
            try {
                execute(requestUnBuilt)
            } catch (e: IOException) {
                DebugLog.e("interceptor : ex", "network_crash", e)
                apiCallData.exception = e.asExceptionData()
                NetworkCallsRepo.set(apiCallData)
                throw e
            }
        callResult.response.save(apiCallData)
        callResult
    }
}

