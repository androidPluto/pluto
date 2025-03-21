package com.pluto.core.notification

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.pluto.core.applifecycle.AppStateCallback

/**
 * Manages debug notifications for Pluto.
 *
 * This class observes application state changes and shows or hides
 * the debug notification accordingly. The notification is shown when
 * the app is in the foreground and hidden when it's in the background.
 *
 * @param application The application instance used for context
 * @param state LiveData that emits application state changes
 */
@SuppressWarnings("UseDataClass")
internal class NotificationManager(application: Application, state: MutableLiveData<AppStateCallback.State>) {

    /** The debug notification that will be shown in the notification drawer */
    private val debugNotification = DebugNotification(application.applicationContext)

    /**
     * Initializes the notification manager by observing app state changes.
     * Shows the notification when the app is in the foreground and
     * hides it when the app is in the background.
     */
    init {
        state.observeForever {
            if (it is AppStateCallback.State.Foreground) {
                debugNotification.add()
            } else {
                debugNotification.remove()
            }
        }
    }
}
