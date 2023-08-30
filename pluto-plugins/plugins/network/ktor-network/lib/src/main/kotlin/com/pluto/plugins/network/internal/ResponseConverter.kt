package com.pluto.plugins.network.internal

import com.pluto.plugins.network.internal.interceptor.logic.ResponseData

internal interface ResponseConverter<T> {
    suspend fun T.convert(): ResponseData
}
