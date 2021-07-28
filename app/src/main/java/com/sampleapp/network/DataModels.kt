package com.sampleapp.network

sealed class ResponseWrapper<out T> {
    data class Success<out T>(val body: T) : ResponseWrapper<T>()
    data class Failure(val error: ErrorResponse, val errorString: String? = null) :
        ResponseWrapper<Nothing>()
}

data class ErrorResponse(val reason: String?, val error: String)

data class StatusResponse(val status: Boolean)
