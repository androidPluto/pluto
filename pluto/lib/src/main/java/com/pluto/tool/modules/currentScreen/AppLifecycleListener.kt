package com.pluto.tool.modules.currentScreen

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * Activity lifecycle callback that tracks the current activity and its fragments.
 *
 * This class implements ActivityLifecycleCallbacks to monitor activity lifecycle events
 * and register fragment lifecycle callbacks when an activity is resumed.
 *
 * @property screenUpdateCallback Callback to notify when the current screen changes
 */
internal class AppLifecycleListener(private val screenUpdateCallback: OnCurrentScreenUpdateListener) : ActivityLifecycleCallbacks {

    /** Fragment lifecycle callback to track fragment changes */
    private val fragmentLifecycleCallbacks = FragmentLifecycleListener(screenUpdateCallback)

    /**
     * Called when an activity is resumed.
     *
     * Updates the current activity name and registers fragment lifecycle callbacks.
     *
     * @param activity The activity that was resumed
     */
    override fun onActivityResumed(activity: Activity) {
        screenUpdateCallback.onUpdate(null, activity::class.java.name)
        fragmentLifecycleCallbacks.activity = activity
        activity.registerFragmentLifecycle(fragmentLifecycleCallbacks)
    }

    /**
     * Called when an activity is paused.
     *
     * Unregisters fragment lifecycle callbacks.
     *
     * @param activity The activity that was paused
     */
    override fun onActivityPaused(activity: Activity) {
        activity.unregisterFragmentLifecycle(fragmentLifecycleCallbacks)
    }

    /** Called when an activity is created. Not used in this implementation. */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    /** Called when an activity is started. Not used in this implementation. */
    override fun onActivityStarted(activity: Activity) {}

    /** Called when an activity is stopped. Not used in this implementation. */
    override fun onActivityStopped(activity: Activity) {}

    /** Called when an activity's state is saved. Not used in this implementation. */
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    /**
     * Called when an activity is destroyed.
     *
     * Clears the reference to the activity in the fragment lifecycle callbacks.
     *
     * @param activity The activity that was destroyed
     */
    override fun onActivityDestroyed(activity: Activity) {
        fragmentLifecycleCallbacks.activity = null
    }
}

/**
 * Registers fragment lifecycle callbacks for an activity.
 *
 * This extension function registers the provided callback with the activity's
 * fragment manager if the activity is a FragmentActivity or AppCompatActivity.
 *
 * @param callback The fragment lifecycle callback to register
 */
private fun Activity.registerFragmentLifecycle(callback: FragmentManager.FragmentLifecycleCallbacks) {
    if (this is FragmentActivity) {
        supportFragmentManager.registerFragmentLifecycleCallbacks(callback, true)
    }
    if (this is AppCompatActivity) {
        supportFragmentManager.registerFragmentLifecycleCallbacks(callback, true)
    }
}

/**
 * Unregisters fragment lifecycle callbacks for an activity.
 *
 * This extension function unregisters the provided callback from the activity's
 * fragment manager if the activity is a FragmentActivity or AppCompatActivity.
 *
 * @param callback The fragment lifecycle callback to unregister
 */
private fun Activity.unregisterFragmentLifecycle(callback: FragmentManager.FragmentLifecycleCallbacks) {
    if (this is FragmentActivity) {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(callback)
    }
    if (this is AppCompatActivity) {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(callback)
    }
}
