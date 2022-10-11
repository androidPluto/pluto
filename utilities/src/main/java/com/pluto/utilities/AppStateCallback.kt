package com.pluto.utilities

import androidx.lifecycle.MutableLiveData

class AppStateCallback {
    val state = MutableLiveData<AppState>()
}

sealed class AppState {
    object Foreground : AppState()
    object Background : AppState()
}
