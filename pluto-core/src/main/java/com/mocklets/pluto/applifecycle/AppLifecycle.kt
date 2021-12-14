package com.mocklets.pluto.applifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.lang.ref.WeakReference

internal class AppLifecycle : ActivityLifecycleCallbacks {

    internal val state: LiveData<AppState>
        get() = _state
    private val _state = MutableLiveData<AppState>()

    private var activityCount = 0
    internal var currentActivity: WeakReference<AppCompatActivity>? = null

    override fun onActivityStarted(activity: Activity) {
        activityCount++
        if (activityCount == 1) {
            _state.postValue(AppState.Foreground)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        activityCount--
        if (activityCount == 0) {
            _state.postValue(AppState.Background)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityResumed(activity: Activity) {
        if (activity is AppCompatActivity) {
            currentActivity = WeakReference(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
