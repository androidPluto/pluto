package com.pluto.core.callback

import androidx.lifecycle.MutableLiveData
import com.pluto.core.applifecycle.AppState

internal class AppStateCallback {
    val state = MutableLiveData<AppState>()
}
