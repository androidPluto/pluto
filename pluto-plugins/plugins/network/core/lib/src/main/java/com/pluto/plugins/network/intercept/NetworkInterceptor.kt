package com.pluto.plugins.network.intercept

import com.pluto.plugins.network.internal.ApiCallData
import com.pluto.plugins.network.internal.MockConfig
import com.pluto.plugins.network.internal.interceptor.logic.NetworkCallsRepo
import com.pluto.plugins.network.internal.interceptor.logic.asExceptionData
import com.pluto.plugins.network.internal.mock.logic.MockSettingsRepo
import com.pluto.utilities.DebugLog
import java.io.IOException
import java.util.UUID

class NetworkInterceptor private constructor(private val request: NetworkData.Request) {
    private val getRequestId: String = UUID.nameUUIDFromBytes("${System.currentTimeMillis()}::${request.url}".toByteArray()).toString()
    private val apiCallData = ApiCallData(id = getRequestId, request = request)
    val requestUrlWithMockInfo: String = MockSettingsRepo.get(request.url, request.method)?.let {
        apiCallData.mock = MockConfig(it)
        NetworkCallsRepo.set(apiCallData)
        it
    } ?: run {
        request.url
    }

    companion object {
        private var instance: NetworkInterceptor? = null

        fun intercept(request: NetworkData.Request): NetworkInterceptor {
            if (instance == null) {
                instance = NetworkInterceptor(request)
            }
            return instance as NetworkInterceptor
        }
    }

    init {
        NetworkCallsRepo.set(apiCallData)
    }

    fun onError(e: IOException) {
        DebugLog.e("pluto.network", "error occurred", e)
        apiCallData.exception = e.asExceptionData()
        NetworkCallsRepo.set(apiCallData)
    }

    fun onResponse(response: NetworkData.Response) {
        apiCallData.response = response
        NetworkCallsRepo.set(apiCallData)
    }
}
