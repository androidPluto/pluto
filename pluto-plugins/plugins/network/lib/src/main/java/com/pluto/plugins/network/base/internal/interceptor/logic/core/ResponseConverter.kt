package com.pluto.plugins.network.base.internal.interceptor.logic.core

import com.pluto.plugins.network.base.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.base.internal.interceptor.logic.NetworkCallsRepo
import com.pluto.plugins.network.base.internal.interceptor.logic.ResponseData

interface ResponseConverter<T> {
    suspend fun T.convert(): ResponseData

    suspend fun T.save(apiCallData: ApiCallData) {
        apiCallData.response = convert()
        NetworkCallsRepo.set(apiCallData)
    }
}
