package com.pluto.plugins.layoutinspector.internal

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.pluto.plugin.lib_interface.PlutoInterface

internal class ActivityLifecycle : ActivityLifecycleCallbacks {

    companion object {
        var topActivity: Activity? = null
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityResumed(activity: Activity) {
        if (activity.javaClass != PlutoInterface.libInfo.pluginActivityClass && activity.javaClass != PlutoInterface.libInfo.selectorActivityClass) {
            topActivity = activity
        }
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        topActivity?.let {
            if (it == activity) {
                topActivity = null
            }
        }
    }
}
