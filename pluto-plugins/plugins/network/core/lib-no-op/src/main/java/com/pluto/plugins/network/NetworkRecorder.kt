package com.pluto.plugins.network

import java.io.IOException

@SuppressWarnings("EmptyFunctionBlock", "UnusedPrivateMember")
class NetworkRecorder(private val request: RequestData) {

    fun onError(e: IOException) {
    }

    fun onResponse(response: ResponseData) {
    }
}
