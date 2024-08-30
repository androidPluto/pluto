package com.pluto.tool.modules.currentScreen

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

internal class AppLifecycleListener(private val screenUpdateCallback: OnCurrentScreenUpdateListener) : ActivityLifecycleCallbacks {

    private val fragmentLifecycleCallbacks = FragmentLifecycleListener(screenUpdateCallback)

    override fun onActivityResumed(activity: Activity) {
        screenUpdateCallback.onUpdate(null, activity::class.java.name)
        fragmentLifecycleCallbacks.activity = activity
        activity.registerFragmentLifecycle(fragmentLifecycleCallbacks)
    }

    override fun onActivityPaused(activity: Activity) {
        activity.unregisterFragmentLifecycle(fragmentLifecycleCallbacks)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        fragmentLifecycleCallbacks.activity = null
    }
}

private fun Activity.registerFragmentLifecycle(callback: FragmentManager.FragmentLifecycleCallbacks) {
    if (this is FragmentActivity) {
        supportFragmentManager.registerFragmentLifecycleCallbacks(callback, true)
    }
    if (this is AppCompatActivity) {
        supportFragmentManager.registerFragmentLifecycleCallbacks(callback, true)
    }
}

private fun Activity.unregisterFragmentLifecycle(callback: FragmentManager.FragmentLifecycleCallbacks) {
    if (this is FragmentActivity) {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(callback)
    }
    if (this is AppCompatActivity) {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(callback)
    }
}
