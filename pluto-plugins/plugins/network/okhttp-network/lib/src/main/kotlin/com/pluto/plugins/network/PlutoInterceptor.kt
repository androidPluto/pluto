package com.pluto.plugins.network

import android.util.Log
import androidx.annotation.Keep
import com.pluto.plugins.network.internal.CacheDirectoryProvider
import com.pluto.plugins.network.internal.ResponseBodyProcessor
import com.pluto.plugins.network.internal.convert
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

@Keep
class PlutoInterceptor : Interceptor {

    private var cacheDirectoryProvider: CacheDirectoryProvider? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        getCacheProvider()?.let { provider ->
            val networkRecorder = NetworkRecorder(request.convert())
            val response: Response = try {
                val builder = request.newBuilder().url(networkRecorder.requestUrlWithMockInfo)
                chain.proceed(builder.build())
            } catch (e: IOException) {
                networkRecorder.onError(e)
                throw e
            }
            return ResponseBodyProcessor.processBody(provider, response) { responseData ->
                networkRecorder.onResponse(responseData)
            }
        }
        Log.e("pluto", "API call not intercepted as Pluto Network is not installed.")
        return chain.proceed(request)
    }

    private fun getCacheProvider(): CacheDirectoryProvider? {
        return PlutoNetwork.applicationContext?.let { context ->
            if (cacheDirectoryProvider == null) {
                cacheDirectoryProvider = CacheDirectoryProvider { context.filesDir }
            }
            cacheDirectoryProvider
        } ?: run {
            null
        }
    }
}
