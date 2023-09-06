package com.sampleapp.functions.network.internal.custom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluto.plugins.network.NetworkRecorder
import com.pluto.plugins.network.ProcessedBody
import com.pluto.plugins.network.RequestData
import com.pluto.plugins.network.ResponseData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CustomViewModel : ViewModel() {

    @SuppressWarnings("MagicNumber")
    fun customTrace() {
        viewModelScope.launch {
            val networkRecorder = NetworkRecorder(
                RequestData(
                    url = "https://google.com",
                    method = "GET",
                    body = ProcessedBody(
                        body = "{\"message\": \"body\"}",
                        contentType = "application/json",
                    ),
                    headers = emptyMap(),
                    sentTimestamp = System.currentTimeMillis()
                )
            )
            delay(5_000)
            networkRecorder.onResponse(
                ResponseData(
                    statusCode = 200,
                    body = ProcessedBody(
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
