package com.mocklets.pluto

import androidx.annotation.Keep
import okhttp3.Interceptor
import okhttp3.Response

@Keep
class PlutoInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request)
    }
}
