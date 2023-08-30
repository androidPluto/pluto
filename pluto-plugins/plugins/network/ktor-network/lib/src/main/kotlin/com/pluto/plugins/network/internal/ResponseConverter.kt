package com.pluto.plugins.network.internal

import com.pluto.plugins.network.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.internal.interceptor.logic.NetworkCallsRepo
import com.pluto.plugins.network.internal.interceptor.logic.ResponseData

internal interface ResponseConverter<T> {
    suspend fun T.convert(): ResponseData

    suspend fun T.save(apiCallData: ApiCallData) {
        apiCallData.response = convert()
        NetworkCallsRepo.set(apiCallData)
    }
}
