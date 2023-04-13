package com.pluto.core.applifecycle

import androidx.lifecycle.MutableLiveData

internal class AppStateCallback {
    val state = MutableLiveData<State>()

    sealed class State {
        object Foreground : State()
        object Background : State()
    }
}
