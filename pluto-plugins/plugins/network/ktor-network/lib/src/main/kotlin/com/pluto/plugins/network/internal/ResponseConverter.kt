package com.pluto.plugins.network.internal

import com.pluto.plugins.network.ResponseData

internal interface ResponseConverter<T> {
    suspend fun T.convert(): ResponseData
}
