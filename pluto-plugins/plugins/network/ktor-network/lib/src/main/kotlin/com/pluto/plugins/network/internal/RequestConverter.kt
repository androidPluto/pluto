package com.pluto.plugins.network.internal

import com.pluto.plugins.network.internal.interceptor.logic.ApiCallData
import com.pluto.plugins.network.internal.interceptor.logic.NetworkCallsRepo
import com.pluto.plugins.network.internal.interceptor.logic.RequestData

internal interface RequestConverter<T> {
    fun T.convert(): RequestData

    fun T.save(): ApiCallData {
        val convertedData = convert()
        val apiCallData = ApiCallData(id = convertedData.url, request = convertedData)
        NetworkCallsRepo.set(apiCallData)
        return apiCallData
    }
}
