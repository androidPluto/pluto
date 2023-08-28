package com.pluto.maven

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluto.core.network.Network
import com.pluto.core.network.ResponseWrapper
import com.pluto.core.network.enqueue
import kotlinx.coroutines.launch

class MavenViewModel : ViewModel() {

    private val apiService: MavenApiService by Network.getService()

    fun getLatestVersion() {
        viewModelScope.launch {
            when (val auth = enqueue { apiService.getLatestVersion() }) {
                is ResponseWrapper.Success -> Log.d("prateek", auth.body.response.docs[0].latestVersion)
                is ResponseWrapper.Failure -> Log.d("prateek", "error")
            }
        }
    }
}
