package com.pluto.plugins.network.ktor.internal

import com.pluto.plugins.network.intercept.NetworkData.Response

internal interface ResponseConverter<T> {
    suspend fun T.convert(): Response
}
