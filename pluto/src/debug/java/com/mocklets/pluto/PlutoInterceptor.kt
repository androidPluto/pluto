package com.mocklets.pluto

import android.util.Log
import androidx.annotation.Keep
import com.mocklets.pluto.core.DebugLog
import com.mocklets.pluto.modules.exceptions.asExceptionData
import com.mocklets.pluto.modules.network.ApiCallData
import com.mocklets.pluto.modules.network.NetworkCallsRepo
import com.mocklets.pluto.modules.network.ProxyConfig
import com.mocklets.pluto.modules.network.interceptor.ResponseBodyProcessor
import com.mocklets.pluto.modules.network.interceptor.convert
import com.mocklets.pluto.modules.network.proxy.NetworkProxyRepo
import com.mocklets.pluto.modules.setup.SetupNotification
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.*

@Keep
class PlutoInterceptor : Interceptor {
    private var setupNotification: SetupNotification? = null

    init {
        Pluto.appContext?.let { context ->
            setupNotification = SetupNotification(context)
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        Pluto.appContext?.let { context ->
            setupNotification?.add()
            val responseBodyProcessor = ResponseBodyProcessor(context)
            val id = UUID.nameUUIDFromBytes("${System.currentTimeMillis()}::${request.url()}".toByteArray()).toString()
            DebugLog.d("interceptor : ot", "$id ${request.url()}")
            val requestData = request.convert()
            val apiCallData = ApiCallData(id = id, request = requestData)
            NetworkCallsRepo.set(apiCallData)

            var proxyRequest: Request? = null
            val proxyUrl = NetworkProxyRepo.get(request.url(), request.method())
            proxyUrl?.let {
                val builder = request.newBuilder().url(it)
                proxyRequest = builder.build()
                apiCallData.proxy = ProxyConfig(proxyUrl)
                NetworkCallsRepo.set(apiCallData)
            }

            val response: Response = try {
                chain.proceed(proxyRequest ?: request)
            } catch (e: IOException) {
                DebugLog.e("interceptor : ex", "network_crash", e)
                apiCallData.exception = e.asExceptionData()
                NetworkCallsRepo.set(apiCallData)
                throw e
            }
            return responseBodyProcessor.processBody(response, apiCallData)
        }
        Log.e("pluto", "API call not intercepted as Pluto is not initialised.")
        return chain.proceed(request)
    }
}
