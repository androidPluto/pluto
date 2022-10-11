package com.pluto.tools.modules.currentScreen

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

internal class AppLifecycleListener(private val appStateCallback: OnCurrentScreenUpdateListener) : ActivityLifecycleCallbacks {
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityResumed(activity: Activity) {
        appStateCallback.onUpdate("fragment", activity.localClassName)
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
