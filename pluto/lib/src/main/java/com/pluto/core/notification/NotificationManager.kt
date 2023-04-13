package com.pluto.core.notification

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.pluto.core.applifecycle.AppStateCallback

@SuppressWarnings("UseDataClass")
internal class NotificationManager(application: Application, state: MutableLiveData<AppStateCallback.State>) {

    private val debugNotification = DebugNotification(application.applicationContext)

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
