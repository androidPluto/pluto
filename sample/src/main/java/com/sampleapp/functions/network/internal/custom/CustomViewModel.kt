package com.sampleapp.functions.network.internal.custom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluto.plugins.network.intercept.NetworkData
import com.pluto.plugins.network.intercept.NetworkInterceptor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CustomViewModel : ViewModel() {

    @SuppressWarnings("MagicNumber")
    fun customTrace() {
        viewModelScope.launch {
            val networkInterceptor = NetworkInterceptor.intercept(
                NetworkData.Request(
                    url = "https://google.com",
                    method = "GET",
                    body = NetworkData.Body(
                        body = "{\"message\": \"body\"}",
                        contentType = "application/json",
                    ),
                    headers = emptyMap(),
                    sentTimestamp = System.currentTimeMillis()
                )
            )
            delay(5_000)
            networkInterceptor.onResponse(
                NetworkData.Response(
                    statusCode = 503,
                    body = NetworkData.Body(
                        body = "body",
                        contentType = "text/plain",
                    ),
                    headers = hashMapOf(
                        "custom_header" to "custom header value"
                    ),
                    sentTimestamp = System.currentTimeMillis(),
                    receiveTimestamp = System.currentTimeMillis() + 1000
                )
            )
        }
    }
}
