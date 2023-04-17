package com.pluto.plugins.network

import android.content.Context
import com.pluto.plugins.network.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.internal.interceptor.logic.NetworkCallsRepo
import com.pluto.plugins.network.internal.interceptor.logic.asExceptionData
import com.pluto.plugins.network.internal.interceptor.logic.core.CacheDirectoryProvider
import java.util.UUID

object PlutoNetwork {
    internal var cacheDirectoryProvider: CacheDirectoryProvider? = null
        private set

    internal fun initialize(context: Context) {
        cacheDirectoryProvider = CacheDirectoryProvider { context.applicationContext.filesDir }
    }

    @JvmOverloads
    fun insertCustomTrace(request: CustomRequest, response: CustomResponse? = null, exception: Throwable? = null) {
        val apiCallData = ApiCallData(
            id = UUID.nameUUIDFromBytes("${System.currentTimeMillis()}::${request.url}".toByteArray()).toString(),
            request = request.toRequestData(),
            response = response?.toResponseData(),
            exception = exception?.asExceptionData(),
            isCustomTrace = true
        )
        NetworkCallsRepo.set(apiCallData)
    }
}
