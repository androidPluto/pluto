package com.sampleapp.functions.network.internal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluto.plugins.network.NetworkRecorder
import com.pluto.plugins.network.ProcessedBody
import com.pluto.plugins.network.RequestData
import com.pluto.plugins.network.ResponseData
import com.sampleapp.functions.network.internal.core.ApiService
import com.sampleapp.functions.network.internal.core.Client
import com.sampleapp.functions.network.internal.core.Network
import com.sampleapp.functions.network.internal.core.PostNewBody
import com.sampleapp.functions.network.internal.core.ResponseWrapper
import com.sampleapp.functions.network.internal.core.enqueue
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class NetworkViewModel : ViewModel() {

    private val apiService: ApiService by Network.getService()
    private val responseData = MutableLiveData<Pair<String, Any?>>()

    fun get() {
        val label = "GET call"
        viewModelScope.launch {
            val auth = enqueue { apiService.get() }
            responseData.postValue(
                when (auth) {
                    is ResponseWrapper.Success -> Pair(label, auth.body)
                    is ResponseWrapper.Failure -> Pair(label, auth.error)
                }
            )
        }
    }

    fun getKtor() {
        viewModelScope.launch {
            Client.get {
                url.path("get")
            }
        }
    }

    fun post() {
        val label = "POST call"
        viewModelScope.launch {
            val auth = enqueue {
                apiService.post(
                    hashMapOf(
                        "user" to "John Smith",
                        "email" to "john.smith@gmail.com"
                    )
                )
            }
            responseData.postValue(
                when (auth) {
                    is ResponseWrapper.Success -> Pair(label, auth.body)
                    is ResponseWrapper.Failure -> Pair(label, auth.error)
                }
            )
        }
    }
    fun postKtor() {
        viewModelScope.launch {
            Client.post {
                contentType(ContentType.Application.Json)
                setBody(
                    PostNewBody("John Smith", "john.smith@gmail.com")
                )
                url.path("post", "new")
            }
        }
    }
    fun xml() {
        val label = "XML Response call"
        val requestBodyText =
            """<html><head>bye</head><body>hello</body></html>""".trimMargin()
        val requestBody = requestBodyText.toRequestBody("text/xml".toMediaTypeOrNull())

        viewModelScope.launch {
            val auth = enqueue {
                apiService.xml(requestBody)
            }

            responseData.postValue(
                when (auth) {
                    is ResponseWrapper.Success -> Pair(label, auth.body)
                    is ResponseWrapper.Failure -> Pair(label, auth.error)
                }
            )
        }
    }

    fun form() {
        val label = "Form URL Encoded call"
        viewModelScope.launch {
            val auth = enqueue {
                apiService.form(title = "sample title", diff = "sample diff")
            }
            responseData.postValue(
                when (auth) {
                    is ResponseWrapper.Success -> Pair(label, auth.body)
                    is ResponseWrapper.Failure -> Pair(label, auth.error)
                }
            )
        }
    }

    @SuppressWarnings("MagicNumber")
    fun customTrace() {
        viewModelScope.launch {
            val networkRecorder = NetworkRecorder(
                RequestData(
                    url = "https://google.com",
                    method = "GET",
                    body = ProcessedBody(
                        body = "body",
                        mediaType = "mediaType.name.lowercase()", // todo fix this
                        mediaSubtype = "mediaSubtype.name.lowercase()"
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
                        mediaType = "mediaType.name.lowercase()",
                        mediaSubtype = "mediaSubtype.name.lowercase()"
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
