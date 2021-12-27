package com.mocklets.pluto.network.internal.proxy

import android.app.Application
import android.webkit.URLUtil
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mocklets.pluto.network.internal.proxy.dao.NetworkProxyEntity
import com.mocklets.pluto.network.internal.proxy.dao.ProxyData
import com.mocklets.pluto.network.internal.pruneQueryParams
import com.mocklets.pluto.plugin.utilities.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class NetworkProxyViewModel(application: Application) : AndroidViewModel(application) {

    val proxyList: LiveData<List<NetworkProxyEntity>>
        get() = _proxyList
    private val _proxyList = MutableLiveData<List<NetworkProxyEntity>>()

    val currentProxy: SingleLiveEvent<NetworkProxyEntity?>
        get() = _currentProxy
    private val _currentProxy = SingleLiveEvent<NetworkProxyEntity?>()

    val event: SingleLiveEvent<Pair<Boolean, String>>
        get() = _event
    private val _event = SingleLiveEvent<Pair<Boolean, String>>()

    val mockletsUrl: SingleLiveEvent<String>
        get() = _mockletsUrl
    private val _mockletsUrl = SingleLiveEvent<String>()

    fun fetchList(search: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            val list = NetworkProxyRepo.fetchList(search)
            _proxyList.postValue(list)
        }
    }

    fun update(requestUrl: String, requestMethod: String, proxyData: ProxyData) {
        if (!URLUtil.isHttpsUrl(proxyData.url)) {
            _event.postValue(Pair(false, "Need https:// URL"))
            return
        }
        if (proxyData.url.length < URL_MIN_LENGTH) { // length of https://
            _event.postValue(Pair(false, "Malformed URL"))
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            NetworkProxyRepo.update(requestUrl.pruneQueryParams(), requestMethod, proxyData)
            _event.postValue(Pair(true, "Proxy Setting updated!"))
        }
    }

    fun fetch(url: String, method: String) {
        _currentProxy.postValue(NetworkProxyRepo.fetch(url.pruneQueryParams(), method))
    }

    fun delete(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            NetworkProxyRepo.delete(url)
            _event.postValue(Pair(true, "Proxy Setting deleted!"))
        }
    }

    fun onInAppBrowserClose() {
        _mockletsUrl.postValue("mock")
    }

    private companion object {
        const val URL_MIN_LENGTH = 9
    }
}
