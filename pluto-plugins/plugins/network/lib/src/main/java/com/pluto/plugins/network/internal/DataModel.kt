package com.pluto.plugins.network.internal

import com.pluto.plugins.network.intercept.NetworkData
import com.pluto.plugins.network.intercept.NetworkInterceptor
import com.pluto.plugins.network.internal.interceptor.logic.ExceptionData
import com.pluto.plugins.network.internal.share.getCurl
import com.pluto.utilities.list.ListItem

internal data class MockConfig(
    val url: String,
)

internal data class Status(
    val code: Int,
    val message: String,
)

internal class ApiCallData(
    val id: String,
    val interceptorOption: NetworkInterceptor.Option,
    val request: NetworkData.Request,
    var response: NetworkData.Response? = null,
    var exception: ExceptionData? = null,
    var mock: MockConfig? = null
) : ListItem() {
    val curl: String
        get() = request.getCurl()
    val responseTime
        get() = exception?.timeStamp ?: response?.receiveTimestamp

    override fun isEqual(other: Any): Boolean {
        if (other is ApiCallData) {
            id == other.id && response == other.response && exception == other.exception
        }
        return false
    }
}
