package com.pluto.plugins.network.okhttp

import androidx.annotation.Keep
import okhttp3.Interceptor
import okhttp3.Response

@SuppressWarnings("UtilityClassWithPublicConstructor")
@Keep
class PlutoOkhttpInterceptor {
    companion object : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            return chain.proceed(request)
        }
    }
}
