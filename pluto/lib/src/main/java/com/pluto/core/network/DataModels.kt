package com.pluto.core.network

import com.squareup.moshi.JsonClass

/**
 * Sealed class that wraps API responses to handle both success and failure cases.
 *
 * This wrapper provides a type-safe way to handle API responses, ensuring that
 * error handling is consistent across the application.
 *
 * @param T The type of data returned in case of success
 */
internal sealed class ResponseWrapper<out T> {
    /**
     * Represents a successful API response.
     *
     * @property body The response data
     */
    data class Success<out T>(val body: T) : ResponseWrapper<T>()

    /**
     * Represents a failed API response.
     *
     * @property error The error response from the API
     * @property errorString Optional additional error information
     */
    data class Failure(val error: ErrorResponse, val errorString: String? = null) :
        ResponseWrapper<Nothing>()
}

/**
 * Data class representing an error response from the API.
 *
 * This class is annotated with JsonClass to generate a Moshi adapter
 * for JSON serialization/deserialization.
 *
 * @property reason Optional reason for the error
 * @property error Error code or message
 */
@JsonClass(generateAdapter = true)
internal data class ErrorResponse(val reason: String?, val error: String)
