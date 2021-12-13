package com.mocklets.pluto

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.mocklets.pluto.notch.Notch

internal class AppLifecycleTracker constructor(private val application: Application) {

    private var activityCount = 0
    private val notch: Notch = Notch(application.applicationContext)
    private val setupNotification = DebugNotification(application.applicationContext)

    private val lifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityStarted(activity: Activity) {
            activityCount++
            if (activityCount == 1) {
                onAppForeground(activity)
            }
        }

        override fun onActivityStopped(activity: Activity) {
            activityCount--
            if (activityCount == 0) {
                onAppBackground()
            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {}
    }

    private fun onAppBackground() {
        notch.remove()
    }

    private fun onAppForeground(activity: Activity) {
        setupNotification.add()
        notch.add(activity.applicationContext)
    }

    fun start(enableNotch: Boolean) {
        if (activityCount > 0) {
            setupNotification.add()
            if (enableNotch) {
                notch.add(application.applicationContext)
            }
        }
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    fun stop() {
        notch.remove()
        setupNotification.remove()
        application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks)
    }
}
