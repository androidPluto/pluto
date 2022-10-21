package com.pluto.core.notification

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.pluto.utilities.AppState

@SuppressWarnings("UseDataClass")
internal class NotificationManager(application: Application, state: MutableLiveData<AppState>) {

    private val debugNotification = DebugNotification(application.applicationContext)

    init {
        state.observeForever {
            if (it is AppState.Foreground) {
                debugNotification.add()
            } else {
                debugNotification.remove()
            }
        }
    }
}
