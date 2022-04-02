package com.sampleapp.plugins.network.internal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sampleapp.plugins.network.internal.core.ApiService
import com.sampleapp.plugins.network.internal.core.Network
import com.sampleapp.plugins.network.internal.core.ResponseWrapper
import com.sampleapp.plugins.network.internal.core.enqueue
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class NetworkViewModel : ViewModel() {

    private val apiService: ApiService by Network.getService()
    private val responseData = MutableLiveData<Pair<String, JsonObject>>()

    fun get() {
        val label = "GET call"
        viewModelScope.launch {
            val auth = enqueue { apiService.get() }
            responseData.postValue(
                when (auth) {
                    is ResponseWrapper.Success -> Pair(label, auth.body)
                    is ResponseWrapper.Failure -> {
                        val string = Gson().toJson(auth.error)
                        Pair(label, Gson().fromJson(string, JsonObject::class.java))
                    }
                }
            )
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
                    is ResponseWrapper.Failure -> {
                        val string = Gson().toJson(auth.error)
                        Pair(label, Gson().fromJson(string, JsonObject::class.java))
                    }
                }
            )
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
                    is ResponseWrapper.Failure -> {
                        val string = Gson().toJson(auth.error)
                        Pair(label, Gson().fromJson(string, JsonObject::class.java))
                    }
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
                    is ResponseWrapper.Failure -> {
                        val string = Gson().toJson(auth.error)
                        Pair(label, Gson().fromJson(string, JsonObject::class.java))
                    }
                }
            )
        }
    }
}
