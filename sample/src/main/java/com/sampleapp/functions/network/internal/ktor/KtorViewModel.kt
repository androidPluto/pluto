package com.sampleapp.functions.network.internal.ktor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path
import kotlinx.coroutines.launch

class KtorViewModel : ViewModel() {

    fun get() {
        viewModelScope.launch {
            Client.get {
                url.path("get")
            }
        }
    }

    fun post() {
        viewModelScope.launch {
            Client.post {
                contentType(ContentType.Application.Json)
                setBody(
                    PostRequestBody("John Smith", "john.smith@gmail.com")
                )
                url.path("post", "new")
            }
        }
    }
}
