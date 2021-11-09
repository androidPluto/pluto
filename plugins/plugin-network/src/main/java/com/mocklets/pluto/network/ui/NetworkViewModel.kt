package com.mocklets.pluto.network.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mocklets.pluto.network.internal.ApiCallData
import com.mocklets.pluto.network.internal.NetworkCallsRepo

internal class NetworkViewModel : ViewModel() {

    val apiCalls: LiveData<List<ApiCallData>>
        get() = NetworkCallsRepo.apiCalls

    private val _currentApiCall = MutableLiveData<ApiCallData>()
    private val _contentSearch = MutableLiveData<String>()

    val detailContentLiveData: LiveData<DetailContentData>
        get() = _detailContentLiveData
    private val _detailContentLiveData = MediatorLiveData<DetailContentData>()

    init {
        _detailContentLiveData.addSource(_currentApiCall) {
            combineData(_currentApiCall, _contentSearch)
        }
        _detailContentLiveData.addSource(_contentSearch) {
            combineData(_currentApiCall, _contentSearch)
        }
    }

    private fun combineData(apiCallLD: MutableLiveData<ApiCallData>, searchLD: MutableLiveData<String>) {
        apiCallLD.value?.let {
            _detailContentLiveData.postValue(DetailContentData(it, searchLD.value))
        }
    }

    fun fetchCurrent(id: String) {
        _currentApiCall.postValue(NetworkCallsRepo.get(id))
    }

    fun searchContent(it: String) {
        _contentSearch.postValue(it.trim())
    }
}

internal data class DetailContentData(
    val api: ApiCallData,
    val search: String?
)
