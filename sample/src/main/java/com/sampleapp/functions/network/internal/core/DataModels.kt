package com.sampleapp.functions.network.internal.core

import com.squareup.moshi.JsonClass

sealed class ResponseWrapper<out T> {
    data class Success<out T>(val body: T) : ResponseWrapper<T>()
    data class Failure(val error: ErrorResponse, val errorString: String? = null) :
        ResponseWrapper<Nothing>()
}

@JsonClass(generateAdapter = true)
data class ErrorResponse(val reason: String?, val error: String)

@JsonClass(generateAdapter = true)
data class StatusResponse(val status: Boolean)
