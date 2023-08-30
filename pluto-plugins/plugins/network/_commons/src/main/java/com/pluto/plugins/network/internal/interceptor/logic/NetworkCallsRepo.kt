package com.pluto.plugins.network.internal.interceptor.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.Collections

object NetworkCallsRepo {

    val apiCalls: LiveData<List<ApiCallData>>
        get() = _apiCalls
    private val _apiCalls = MutableLiveData<List<ApiCallData>>()
    private val apiCallMap = Collections.synchronizedMap(LinkedHashMap<String, ApiCallData>())

    fun set(request: ApiCallData) {
        synchronized(apiCallMap) {
            apiCallMap[request.id] = request
            apiCallMap.updateLiveData()
        }
    }

    fun get(id: String): ApiCallData? {
        return synchronized(apiCallMap) {
            apiCallMap.getOrElse(id) { null }
        }
    }

    fun deleteAll() {
        synchronized(apiCallMap) {
            apiCallMap.clear()
            apiCallMap.updateLiveData()
        }
    }

    private fun MutableMap<String, ApiCallData>.updateLiveData() {
        val list = arrayListOf<ApiCallData>()
        list.addAll(values)
        list.reverse()
        _apiCalls.postValue(list)
    }
}
