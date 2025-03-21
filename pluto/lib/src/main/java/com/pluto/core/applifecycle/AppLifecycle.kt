package com.pluto.core.applifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

/**
 * Tracks application lifecycle events to determine when the app is in foreground or background.
 *
 * This class implements ActivityLifecycleCallbacks to monitor activity start and stop events,
 * maintaining a count of active activities to determine the overall app state.
 *
 * @property appStateCallback Callback to notify when app state changes between foreground and background
 */
internal class AppLifecycle(private val appStateCallback: AppStateCallback) : ActivityLifecycleCallbacks {

    /** Counter to track the number of started (visible) activities */
    private var activityCount = 0

    /**
     * Called when an activity is started.
     *
     * Increments the activity counter and updates app state to foreground
     * when the first activity becomes visible.
     *
     * @param activity The activity that was started
     */
    override fun onActivityStarted(activity: Activity) {
        activityCount++
        if (activityCount == 1) {
            appStateCallback.state.postValue(AppStateCallback.State.Foreground)
        }
    }

    /**
     * Called when an activity is stopped.
     *
     * Decrements the activity counter and updates app state to background
     * when no activities are visible.
     *
     * @param activity The activity that was stopped
     */
    override fun onActivityStopped(activity: Activity) {
        activityCount--
        if (activityCount == 0) {
            appStateCallback.state.postValue(AppStateCallback.State.Background)
        }
    }

    /** Called when an activity is created. Not used in this implementation. */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    /** Called when an activity is resumed. Not used in this implementation. */
    override fun onActivityResumed(activity: Activity) {}

    /** Called when an activity is paused. Not used in this implementation. */
    override fun onActivityPaused(activity: Activity) {}

    /** Called when an activity's state is saved. Not used in this implementation. */
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    /** Called when an activity is destroyed. Not used in this implementation. */
    override fun onActivityDestroyed(activity: Activity) {}
}
