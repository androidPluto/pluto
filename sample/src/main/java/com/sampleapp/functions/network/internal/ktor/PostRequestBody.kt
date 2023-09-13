package com.sampleapp.functions.network.internal.ktor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostRequestBody(
    @SerialName("user")
    val user: String,
    @SerialName("email")
    val email: String,
)
