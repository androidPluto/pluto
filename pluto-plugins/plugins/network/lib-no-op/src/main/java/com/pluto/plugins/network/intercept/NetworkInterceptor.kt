package com.pluto.plugins.network.intercept

import java.io.IOException

@SuppressWarnings("EmptyFunctionBlock", "UnusedPrivateMember")
class NetworkInterceptor private constructor(private val request: NetworkData.Request, option: Option) {

    companion object {

        @JvmOverloads
        fun intercept(request: NetworkData.Request, option: Option = Option()): NetworkInterceptor {
            return NetworkInterceptor(request, option)
        }
    }

    fun onError(e: IOException) {
    }

    fun onResponse(response: NetworkData.Response) {
    }

    data class Option(
        val name: String = "Custom",
        val metadata: HashMap<String, String>? = null
    )
}
