package com.pluto.core.network

import com.squareup.moshi.JsonClass

internal sealed class ResponseWrapper<out T> {
    data class Success<out T>(val body: T) : ResponseWrapper<T>()
    data class Failure(val error: ErrorResponse, val errorString: String? = null) :
        ResponseWrapper<Nothing>()
}

@JsonClass(generateAdapter = true)
internal data class ErrorResponse(val reason: String?, val error: String)
