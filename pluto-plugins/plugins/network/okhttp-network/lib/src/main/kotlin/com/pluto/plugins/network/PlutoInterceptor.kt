package com.pluto.plugins.network

import android.util.Log
import androidx.annotation.Keep
import com.pluto.plugins.network.internal.ResponseBodyProcessor
import com.pluto.plugins.network.internal.convert
import com.pluto.plugins.network.internal.mock.logic.MockSettingsRepo
import com.pluto.plugins.network.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.internal.interceptor.logic.MockConfig
import com.pluto.plugins.network.internal.interceptor.logic.NetworkCallsRepo
import com.pluto.plugins.network.internal.interceptor.logic.asExceptionData
import com.pluto.plugins.network.internal.interceptor.logic.core.getRequestID
import com.pluto.utilities.DebugLog
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

@Keep
class PlutoInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        PlutoNetwork.cacheDirectoryProvider?.let { provider ->
            val id = getRequestID(request.url.toString())
            DebugLog.d("interceptor : ot", "$id ${request.url}")
            val requestData = request.convert()
            val apiCallData = ApiCallData(id = id, request = requestData)
            NetworkCallsRepo.set(apiCallData)

            var mockRequest: Request? = null
            val mockUrl = MockSettingsRepo.get(
                url = request.url.toString(),
                method = request.method
            )
            mockUrl?.let {
                val builder = request.newBuilder().url(it)
                mockRequest = builder.build()
                apiCallData.mock = MockConfig(mockUrl)
                NetworkCallsRepo.set(apiCallData)
            }

            val response: Response = try {
                chain.proceed(mockRequest ?: request)
            } catch (e: IOException) {
                DebugLog.e("interceptor : ex", "network_crash", e)
                apiCallData.exception = e.asExceptionData()
                NetworkCallsRepo.set(apiCallData)
                throw e
            }
            return ResponseBodyProcessor.processBody(provider, response, apiCallData)
        }
        Log.e("pluto", "API call not intercepted as Pluto Network is not installed.")
        return chain.proceed(request)
    }
}
