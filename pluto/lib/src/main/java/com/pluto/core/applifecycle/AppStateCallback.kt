package com.pluto.core.applifecycle

import androidx.lifecycle.MutableLiveData

/**
 * Callback for tracking and notifying application state changes.
 *
 * This class provides a LiveData object that emits state changes when the application
 * moves between foreground and background states.
 */
internal class AppStateCallback {
    /** LiveData that emits the current application state (foreground or background) */
    val state = MutableLiveData<State>()

    /**
     * Sealed class representing possible application states.
     *
     * The application can be either in the foreground (at least one activity visible)
     * or in the background (no activities visible).
     */
    sealed class State {
        /** Application is in the foreground (at least one activity is visible) */
        object Foreground : State()

        /** Application is in the background (no activities are visible) */
        object Background : State()
    }
}
