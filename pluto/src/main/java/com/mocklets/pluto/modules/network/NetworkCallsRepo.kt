package com.mocklets.pluto.modules.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.Collections

internal object NetworkCallsRepo {

    val apiCalls: LiveData<List<ApiCallData>>
        get() = _apiCalls
    private val _apiCalls = MutableLiveData<List<ApiCallData>>()
    private val apiCallMap = Collections.synchronizedMap(LinkedHashMap<String, ApiCallData>())

    fun set(request: ApiCallData) {
        apiCallMap[request.id] = request
        apiCallMap.updateLiveData()
    }

//    fun saveProxyRequest(id: String, proxy: ProxyConfig) {
//        apiCallMap[id]?.let {
//            apiCallMap[id] = ApiCallData(it.id, it.request, it.response, it.exception, proxy)
//            apiCallMap.updateLiveData()
//        }
//    }

    private fun MutableMap<String, ApiCallData>.updateLiveData() {
        val list = arrayListOf<ApiCallData>()
        list.addAll(values)
        list.reverse()
        _apiCalls.postValue(list)
    }

//    fun saveResponse(id: String, response: ResponseData) {
//        apiCallMap[id]?.let {
//            apiCallMap[id] = ApiCallData(it.id, it.request, response, it.exception, it.proxy)
//            apiCallMap.updateLiveData()
//        }
//    }

    fun get(id: String): ApiCallData? {
        return apiCallMap.getOrElse(id) { null }
    }

//    fun saveException(id: String, exception: ExceptionData) {
//        apiCallMap[id]?.let {
//            apiCallMap[id] = ApiCallData(it.id, it.request, it.response, exception, it.proxy)
//            apiCallMap.updateLiveData()
//        }
//    }

    fun deleteAll() {
        apiCallMap.clear()
        apiCallMap.updateLiveData()
    }
}
