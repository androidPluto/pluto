package com.sampleapp.functions.network.internal.okhttp

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

sealed class ResponseWrapper<out T> {
    data class Success<out T>(val body: T) : ResponseWrapper<T>()
    data class Failure(val error: ErrorResponse, val errorString: String? = null) :
        ResponseWrapper<Nothing>()
}

@Serializable
@JsonClass(generateAdapter = true)
data class ErrorResponse(val reason: String?, val error: String)
