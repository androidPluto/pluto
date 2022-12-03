package com.pluto.core.applifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.pluto.ui.container.PlutoActivity

internal class AppLifecycle(private val appStateCallback: AppStateCallback) : ActivityLifecycleCallbacks {

    private var activityCount = 0

    override fun onActivityStarted(activity: Activity) {
        activityCount++
        if (activityCount == 1) {
            appStateCallback.state.postValue(AppStateCallback.State.Foreground)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        activityCount--
        if (activityCount == 0) {
            appStateCallback.state.postValue(AppStateCallback.State.Background)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
