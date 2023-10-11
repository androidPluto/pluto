package com.pluto.maven

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluto.BuildConfig
import com.pluto.core.network.Network
import com.pluto.core.network.ResponseWrapper
import com.pluto.core.network.enqueue
import com.pluto.utilities.SingleLiveEvent
import kotlinx.coroutines.launch

class MavenViewModel : ViewModel() {

    private val apiService: MavenApiService by Network.getService()
    val latestVersion: LiveData<String>
        get() = _latestVersion
    private val _latestVersion = com.pluto.utilities.SingleLiveEvent<String>()

    fun getLatestVersion() {
        viewModelScope.launch {
            if (!MavenSession.alreadySynced) {
                MavenSession.latestVersion = when (val auth = enqueue { apiService.getLatestVersion() }) {
                    is ResponseWrapper.Success -> retrieveLatestResponse(auth.body.response)
                    is ResponseWrapper.Failure -> null
                }
                MavenSession.alreadySynced = true
            }
            MavenSession.latestVersion?.let {
                _latestVersion.postValue(it)
            }
        }
    }

    private fun retrieveLatestResponse(response: MavenResponse): String? {
        return if (response.docs.isNotEmpty() && BuildConfig.VERSION_NAME != response.docs[0].latestVersion) {
            response.docs[0].latestVersion
        } else {
            null
        }
    }
}
