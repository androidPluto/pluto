package com.pluto.applifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.DebugNotification

class AppLifecycle : ActivityLifecycleCallbacks {

    internal val state: LiveData<AppState>
        get() = _state
    private val _state = MutableLiveData<AppState>()

    private var activityCount = 0

    override fun onActivityStarted(activity: Activity) {
        activityCount++
        if (activityCount == 1) {
            _state.postValue(AppState.Foreground)
            DebugNotification(activity.applicationContext).add()
        }
    }

    override fun onActivityStopped(activity: Activity) {
        activityCount--
        if (activityCount == 0) {
            _state.postValue(AppState.Background)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
