package com.pluto.plugins.network

import com.pluto.plugins.network.internal.interceptor.logic.NetworkCallsRepo
import com.pluto.plugins.network.internal.interceptor.logic.asExceptionData
import com.pluto.plugins.network.internal.mock.logic.MockSettingsRepo
import com.pluto.utilities.DebugLog
import java.io.IOException
import java.util.UUID

class NetworkRecorder(private val request: RequestData) {
    private val getRequestId: String = UUID.nameUUIDFromBytes("${System.currentTimeMillis()}::${request.url}".toByteArray()).toString()
    private val apiCallData = ApiCallData(id = getRequestId, request = request)
    val requestUrlWithMockInfo: String = MockSettingsRepo.get(request.url, request.method)?.let {
        apiCallData.mock = MockConfig(it)
        NetworkCallsRepo.set(apiCallData)
        it
    } ?: run {
        request.url
    }

    init {
        NetworkCallsRepo.set(apiCallData)
    }

    fun onError(e: IOException) {
        DebugLog.e("pluto.network", "error occurred", e)
        apiCallData.exception = e.asExceptionData()
        NetworkCallsRepo.set(apiCallData)
    }

    fun onResponse(response: ResponseData) {
        apiCallData.response = response
        NetworkCallsRepo.set(apiCallData)
    }
}
