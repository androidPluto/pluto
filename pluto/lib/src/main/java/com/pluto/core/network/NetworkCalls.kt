package com.pluto.core.network

import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 * Executes a network API call and wraps the response in a ResponseWrapper.
 *
 * This function handles exceptions that may occur during the API call and
 * converts them into appropriate error responses. It uses coroutines to
 * perform the network call on the specified dispatcher.
 *
 * @param dispatcher The coroutine dispatcher to use for the API call (defaults to IO)
 * @param apiCall The suspend function that makes the actual API call
 * @return A ResponseWrapper containing either the successful result or an error
 */
@Suppress("TooGenericExceptionCaught")
internal suspend fun <T> enqueue(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    apiCall: suspend () -> T
): ResponseWrapper<T> {

    return withContext(dispatcher) {
        try {
            ResponseWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            Log.e("network_error", "network failure", throwable)
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

/**
 * Converts an HTTP exception to an ErrorResponse object.
 *
 * This function attempts to parse the error body of an HTTP exception
 * into an ErrorResponse object using Moshi. If parsing fails, it returns
 * a default error response.
 *
 * @param throwable The HTTP exception to convert
 * @return An ErrorResponse object representing the error
 */
@Suppress("TooGenericExceptionCaught")
private fun convertErrorBody(throwable: HttpException): ErrorResponse {
    val moshiAdapter: JsonAdapter<ErrorResponse> = Moshi.Builder().build().adapter(ErrorResponse::class.java)
    val errorString = throwable.response()?.errorBody()?.string()
    return if (!errorString.isNullOrEmpty()) {
        try {
            run {
                val error = moshiAdapter.fromJson(errorString)
                validateError(error)
                error ?: ErrorResponse(VALIDATION_ERROR_MESSAGE, DEFAULT_ERROR_MESSAGE)
            }
        } catch (exception: Exception) {
            Log.e(
                "network_error",
                exception.message.toString(),
                exception
            )
            ErrorResponse(CONVERSION_FAILURE, DEFAULT_ERROR_MESSAGE)
        }
    } else {
        ErrorResponse(UPSTREAM_FAILURE, EMPTY_ERROR_MESSAGE)
    }
}

/**
 * Validates that an ErrorResponse object has a non-null error field.
 *
 * @param error The ErrorResponse object to validate
 * @throws KotlinNullPointerException if the error field is null
 */
private fun validateError(error: ErrorResponse?) {
    if (error?.error == null) { // TODO handle deserialization issue
        throw KotlinNullPointerException("response.error value null")
    }
}

/** Default error message for general errors */
private const val DEFAULT_ERROR_MESSAGE = "Something went wrong!"

/** Error message for empty error responses */
private const val EMPTY_ERROR_MESSAGE = "empty error response"

/** Error code for validation errors */
private const val VALIDATION_ERROR_MESSAGE = "validation_error_message"

/** Error code for upstream failures */
private const val UPSTREAM_FAILURE = "upstream_failure"

/** Error code for response conversion failures */
private const val CONVERSION_FAILURE = "response_conversion_failure"
