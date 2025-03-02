package com.pluto.plugins.network.internal.mock.ui

import android.app.Application
import android.webkit.URLUtil
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pluto.plugins.network.internal.interceptor.logic.pruneQueryParams
import com.pluto.plugins.network.internal.mock.logic.MockSettingsRepo
import com.pluto.plugins.network.internal.mock.logic.dao.MockData
import com.pluto.plugins.network.internal.mock.logic.dao.MockSettingsEntity
import com.pluto.utilities.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class MockSettingsViewModel(application: Application) : AndroidViewModel(application) {

    val mockList: LiveData<List<MockSettingsEntity>>
        get() = _mockList
    private val _mockList = MutableLiveData<List<MockSettingsEntity>>()

    val currentMock: SingleLiveEvent<MockSettingsEntity?>
        get() = _currentMock
    private val _currentMock = SingleLiveEvent<MockSettingsEntity?>()

    val event: SingleLiveEvent<Pair<Boolean, String>>
        get() = _event
    private val _event = SingleLiveEvent<Pair<Boolean, String>>()

    val mockletsUrlEvent: SingleLiveEvent<String>
        get() = _mockletsUrlEvent
    private val _mockletsUrlEvent = SingleLiveEvent<String>()

    fun fetchList(search: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            val list = MockSettingsRepo.fetchList(search)
            _mockList.postValue(list)
        }
    }

    fun update(requestUrl: String, requestMethod: String, mockData: MockData) {
        if (!URLUtil.isHttpsUrl(mockData.url)) {
            _event.postValue(Pair(false, "Need https:// URL"))
            return
        }
        if (mockData.url.length < URL_MIN_LENGTH) { // length of https://
            _event.postValue(Pair(false, "Malformed URL"))
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            MockSettingsRepo.update(requestUrl.pruneQueryParams(), requestMethod, mockData)
            _event.postValue(Pair(true, "Mock Setting updated!"))
        }
    }

    fun fetch(url: String?, method: String?) {
        if (url != null && method != null) {
            _currentMock.postValue(MockSettingsRepo.fetch(url.pruneQueryParams(), method))
        }
    }

    fun delete(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            MockSettingsRepo.delete(url)
            _event.postValue(Pair(true, "Mock Setting deleted!"))
        }
    }

    fun onInAppBrowserClose() {
        _mockletsUrlEvent.postValue("mock")
    }

    private companion object {
        const val URL_MIN_LENGTH = 9
    }
}
