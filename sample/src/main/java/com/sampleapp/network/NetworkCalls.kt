package com.sampleapp.network

import com.google.gson.Gson
import com.mocklets.pluto.utilities.DebugLog
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

@Suppress("TooGenericExceptionCaught")
suspend fun <T> enqueue(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): ResponseWrapper<T> {

    return withContext(dispatcher) {
        try {
            ResponseWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            DebugLog.e(tag = "network_error", message = "network failure", tr = throwable)
            when (throwable) {
                is IOException -> ResponseWrapper.Failure(
                    ErrorResponse(
                        "IO_Exception", throwable.message ?: DEFAULT_ERROR_MESSAGE
                    )
                )
                is HttpException -> ResponseWrapper.Failure(convertErrorBody(throwable))
                else -> ResponseWrapper.Failure(
                    ErrorResponse(CONVERSION_FAILURE, DEFAULT_ERROR_MESSAGE)
                )
            }
        }
    }
}

@Suppress("TooGenericExceptionCaught")
private fun convertErrorBody(throwable: HttpException): ErrorResponse {
    val errorString = throwable.response()?.errorBody()?.string()
    return if (!errorString.isNullOrEmpty()) {
        try {
            run {
                val error = Gson().fromJson(errorString, ErrorResponse::class.java)
                validateError(error)
                error
            }
        } catch (exception: Exception) {
            DebugLog.e(
                tag = "network_error",
                message = exception.message.toString(),
                tr = exception
            )
            ErrorResponse(CONVERSION_FAILURE, DEFAULT_ERROR_MESSAGE)
        }
    } else {
        ErrorResponse(UPSTREAM_FAILURE, EMPTY_ERROR_MESSAGE)
    }
}

private fun validateError(error: ErrorResponse) {
    if (error.error == null) { // TODO handle deserialization issue
        throw KotlinNullPointerException("response.error value null")
    }
}

private const val DEFAULT_ERROR_MESSAGE = "Something went wrong!"
private const val EMPTY_ERROR_MESSAGE = "empty error response"
private const val UPSTREAM_FAILURE = "upstream_failure"
private const val CONVERSION_FAILURE = "response_conversion_failure"
