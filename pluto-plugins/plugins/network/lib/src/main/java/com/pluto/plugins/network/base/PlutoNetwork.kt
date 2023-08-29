package com.pluto.plugins.network.base

import android.content.Context
import com.pluto.plugins.network.base.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.base.internal.interceptor.logic.NetworkCallsRepo
import com.pluto.plugins.network.base.internal.interceptor.logic.asExceptionData
import com.pluto.plugins.network.base.internal.interceptor.logic.core.CacheDirectoryProvider
import com.pluto.utilities.DebugLog
import java.util.UUID

object PlutoNetwork {
    var cacheDirectoryProvider: CacheDirectoryProvider? = null
        private set

    internal fun initialize(context: Context) {
        cacheDirectoryProvider = CacheDirectoryProvider { context.applicationContext.filesDir }
    }

    /**
     * Log a custom network trace to Pluto.
     * Allows to connect non-OkHttp based systems to track network calls.
     * @see <a href="https://github.com/androidPluto/pluto/issues/8">https://github.com/plutolib/pluto/issues/8</a>
     *
     * @param request custom request data
     * @param response custom response data
     * @param exception failure exception
     */
    @JvmOverloads
    fun logCustomTrace(request: CustomRequest, response: CustomResponse? = null, exception: Throwable? = null) {
        if (response == null && exception == null) {
            DebugLog.e("pluto_network", "Skipping custom trace logging! Response & Exception both cannot be null at once.")
        } else {
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
}
