package com.mocklets.pluto.modules.activities

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.mocklets.pluto.core.DebugLog
import com.mocklets.pluto.modules.setup.SetupNotification
import com.mocklets.pluto.modules.setup.easyaccess.Popup
import com.mocklets.pluto.ui.PlutoActivity

internal class ActivityTracker @JvmOverloads constructor(
    application: Application,
    shouldShowIntroToast: Boolean = true
) {

    private var isCustomTabOpened: Boolean = false
    private var activityCount = 0
    private val popup: Popup = Popup(application.applicationContext, shouldShowIntroToast)
    private val setupNotification = SetupNotification(application.applicationContext)

    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentCreated(manager: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {
            DebugLog.d(LOG_TAG, ">> created ${fragment.javaClass.simpleName}")
        }

        override fun onFragmentAttached(manager: FragmentManager, fragment: Fragment, context: Context) {
            DebugLog.d(LOG_TAG, ">> attached ${fragment.javaClass.simpleName}")
        }

        override fun onFragmentStarted(manager: FragmentManager, fragment: Fragment) {
            DebugLog.d(LOG_TAG, ">> started ${fragment.javaClass.simpleName}")
        }

        override fun onFragmentResumed(manager: FragmentManager, fragment: Fragment) {
            DebugLog.d(LOG_TAG, ">> resumed ${fragment.javaClass.simpleName}")
        }

        override fun onFragmentPaused(manager: FragmentManager, fragment: Fragment) {
            DebugLog.d(LOG_TAG, ">> paused ${fragment.javaClass.simpleName}")
        }

        override fun onFragmentStopped(manager: FragmentManager, fragment: Fragment) {
            DebugLog.d(LOG_TAG, ">> stopped ${fragment.javaClass.simpleName}")
        }

        override fun onFragmentViewCreated(manager: FragmentManager, fragment: Fragment, v: View, state: Bundle?) {
            DebugLog.d(LOG_TAG, ">> view_created ${fragment.javaClass.simpleName}")
        }

        override fun onFragmentViewDestroyed(manager: FragmentManager, fragment: Fragment) {
            DebugLog.d(LOG_TAG, ">> view_destroyed ${fragment.javaClass.simpleName}")
        }

        override fun onFragmentDetached(manager: FragmentManager, fragment: Fragment) {
            DebugLog.d(LOG_TAG, ">> detached ${fragment.javaClass.simpleName}")
        }

        override fun onFragmentDestroyed(manager: FragmentManager, fragment: Fragment) {
            DebugLog.d(LOG_TAG, ">> destroyed ${fragment.javaClass.simpleName}")
        }
    }

    private val lifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            DebugLog.d(LOG_TAG, "create ${activity.javaClass.simpleName}")
            activity.registerFragmentLifecycle(fragmentLifecycleCallbacks)
        }

        override fun onActivityStarted(activity: Activity) {
            DebugLog.d(LOG_TAG, "start ${activity.javaClass.simpleName}")
            activityCount++
            if (activityCount == 1) {
                onAppForeground(activity)
                setupNotification.add()
            }
        }

        override fun onActivityResumed(activity: Activity) {
            DebugLog.d(LOG_TAG, "resume ${activity.javaClass.simpleName}")
            if (activity is PlutoActivity) {
                popup.remove()
            } else {
                popup.add(activity)
            }
        }

        override fun onActivityPaused(activity: Activity) {
            DebugLog.d(LOG_TAG, "pause ${activity.javaClass.simpleName}")
        }

        override fun onActivityStopped(activity: Activity) {
            DebugLog.d(LOG_TAG, "stop ${activity.javaClass.simpleName}")
            activityCount--
            if (activityCount == 0) {
                if (!isCustomTabOpened && activity is PlutoActivity) {
                    activity.finish()
                }
                onAppBackground()
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {
            if (activity is AppCompatActivity) {
                activity.unregisterFragmentLifecycle(fragmentLifecycleCallbacks)
            }
            if (activity !is PlutoActivity && activityCount == 0) {
                popup.remove()
//                setupNotification.remove()
            }
            DebugLog.d(LOG_TAG, "destroy ${activity.javaClass.simpleName}")
        }

//        override fun onActivityPostDestroyed(activity: Activity) {
//            if (activity is AppCompatActivity) {
//                activity.unregisterFragmentLifecycle(fragmentLifecycleCallbacks)
//            }
//            if (activity !is PlutoActivity && activityCount == 0) {
//                popup.remove()
// //                setupNotification.remove()
//            }
//            DebugLog.d(LOG_TAG, "post destroy ${activity.javaClass.simpleName}")
//            super.onActivityPostDestroyed(activity)
//        }
    }

    private fun onAppBackground() {
        popup.remove()
    }

    private fun onAppForeground(activity: Activity) {
        DebugLog.d(LOG_TAG, "app_foreground ${activity.javaClass.simpleName}")
    }

    init {
        registerActivityLifecycle(application)
    }

    private fun registerActivityLifecycle(application: Application) {
        application.registerActivityLifecycleCallbacks(lifecycleCallbacks)
    }

    fun customTabOpened() {
        isCustomTabOpened = true
    }

    fun customTabClosed() {
        isCustomTabOpened = false
    }

    companion object {
        const val LOG_TAG = "activity_tracker"
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
