package com.pluto.core.notch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.pluto.core.applifecycle.AppStateCallback

internal class NotchStateCallback(appState: LiveData<AppStateCallback.State>, selectorState: LiveData<Boolean>) {
    val state = MediatorLiveData<Boolean>()

    init {
        state.addSource(selectorState) {
            state.postValue(getState(appState.value, selectorState.value))
        }
        state.addSource(appState) {
            state.postValue(getState(appState.value, selectorState.value))
        }
    }

    private fun getState(state: AppStateCallback.State?, showing: Boolean?): Boolean {
        state?.let {
            return if (it is AppStateCallback.State.Background) {
                false
            } else {
                !(showing ?: false)
            }
        }
        return false
    }
}
