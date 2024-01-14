package com.pluto.core.applifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal class AppLifecycle(private val appStateCallback: AppStateCallback) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        appStateCallback.state.postValue(AppStateCallback.State.Foreground)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        appStateCallback.state.postValue(AppStateCallback.State.Background)
    }
}
