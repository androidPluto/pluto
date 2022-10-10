package com.pluto.core.callback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.pluto.core.applifecycle.AppState

internal class NotchStateCallback(appState: LiveData<AppState>, selectorState: LiveData<Boolean>) {
    val state = MediatorLiveData<Boolean>()

    init {
        state.addSource(selectorState) {
            state.postValue(getState(appState.value, selectorState.value))
        }
        state.addSource(appState) {
            state.postValue(getState(appState.value, selectorState.value))
        }
    }

    private fun getState(state: AppState?, showing: Boolean?): Boolean {
        state?.let {
            return if (it is AppState.Background) {
                false
            } else {
                !(showing ?: false)
            }
        }
        return false
    }
}
