package com.pluto.plugins.network.okhttp.internal

import androidx.annotation.Keep
import com.pluto.plugins.network.intercept.NetworkInterceptor
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

@Keep
internal class PlutoInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val networkInterceptor = NetworkInterceptor.intercept(request.convert(), NetworkInterceptor.Option(NAME))
        val response: Response = try {
            val builder = request.newBuilder().url(networkInterceptor.requestUrlWithMockInfo)
            chain.proceed(builder.build())
        } catch (e: IOException) {
            networkInterceptor.onError(e)
            throw e
        }
        return ResponseBodyProcessor.processBody(response) { responseData ->
            networkInterceptor.onResponse(responseData)
        }
    }

    companion object {
        private const val NAME = "Okhttp"
    }
}
