package com.pluto.core.notch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.pluto.core.applifecycle.AppStateCallback

/**
 * Callback that determines when the notch UI component should be visible.
 *
 * This class combines app state (foreground/background) and selector state (showing/hidden)
 * to determine whether the notch should be visible. The notch is only shown when the app
 * is in the foreground and the selector is not visible.
 *
 * @param appState LiveData that emits the current application state
 * @param selectorState LiveData that indicates whether the selector UI is visible
 */
internal class NotchStateCallback(appState: LiveData<AppStateCallback.State>, selectorState: LiveData<Boolean>) {
    /**
     * LiveData that emits whether the notch should be visible.
     * True indicates the notch should be shown, false indicates it should be hidden.
     */
    val state = MediatorLiveData<Boolean>()

    /**
     * Initializes the state by observing both app state and selector state.
     * When either changes, the notch visibility state is recalculated.
     */
    init {
        state.addSource(selectorState) {
            state.postValue(getState(appState.value, selectorState.value))
        }
        state.addSource(appState) {
            state.postValue(getState(appState.value, selectorState.value))
        }
    }

    /**
     * Determines whether the notch should be visible based on app state and selector state.
     *
     * The notch is visible only when:
     * 1. The app is in the foreground
     * 2. The selector UI is not visible
     *
     * @param state The current application state (foreground/background)
     * @param showing Whether the selector UI is currently visible
     * @return True if the notch should be visible, false otherwise
     */
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
