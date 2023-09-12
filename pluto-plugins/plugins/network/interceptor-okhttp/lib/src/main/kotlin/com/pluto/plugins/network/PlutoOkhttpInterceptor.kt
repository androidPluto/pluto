package com.pluto.plugins.network

import android.util.Log
import androidx.annotation.Keep
import com.pluto.plugins.network.intercept.NetworkInterceptor
import com.pluto.plugins.network.internal.CacheDirectoryProvider
import com.pluto.plugins.network.internal.ResponseBodyProcessor
import com.pluto.plugins.network.internal.convert
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

@Keep
class PlutoOkhttpInterceptor : Interceptor {

    private var cacheDirectoryProvider: CacheDirectoryProvider? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        getCacheProvider()?.let { provider ->
            val networkInterceptor = NetworkInterceptor.intercept(request.convert(), NetworkInterceptor.Option(NAME))
            val response: Response = try {
                val builder = request.newBuilder().url(networkInterceptor.requestUrlWithMockInfo)
                chain.proceed(builder.build())
            } catch (e: IOException) {
                networkInterceptor.onError(e)
                throw e
            }
            return ResponseBodyProcessor.processBody(provider, response) { responseData ->
                networkInterceptor.onResponse(responseData)
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

    companion object {
        private const val NAME = "Okhttp"
    }
}
