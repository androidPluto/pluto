package com.pluto.tool.modules.currentScreen

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Fragment lifecycle callback that tracks the current fragment.
 *
 * This class implements FragmentManager.FragmentLifecycleCallbacks to monitor
 * fragment lifecycle events and update the current screen information when
 * a fragment is resumed.
 *
 * @property screenUpdateCallback Callback to notify when the current screen changes
 */
internal class FragmentLifecycleListener(private val screenUpdateCallback: OnCurrentScreenUpdateListener) : FragmentManager.FragmentLifecycleCallbacks() {
    /**
     * Reference to the current activity.
     *
     * This property is set by the AppLifecycleListener when an activity is resumed
     * and cleared when the activity is destroyed.
     */
    var activity: Activity? = null

    /** Called when a fragment is created. Not used in this implementation. */
    override fun onFragmentCreated(manager: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {
    }

    /** Called when a fragment is attached to its context. Not used in this implementation. */
    override fun onFragmentAttached(manager: FragmentManager, fragment: Fragment, context: Context) {
    }

    /** Called when a fragment is started. Not used in this implementation. */
    override fun onFragmentStarted(manager: FragmentManager, fragment: Fragment) {
    }

    /**
     * Called when a fragment is resumed.
     *
     * Updates the current screen information with the fragment and activity names.
     *
     * @param manager The fragment manager
     * @param fragment The fragment that was resumed
     */
    override fun onFragmentResumed(manager: FragmentManager, fragment: Fragment) {
        screenUpdateCallback.onUpdate(fragment::class.java.name, activity?.let { it::class.java.name } ?: run { null })
    }

    /** Called when a fragment is paused. Not used in this implementation. */
    override fun onFragmentPaused(manager: FragmentManager, fragment: Fragment) {
    }

    /** Called when a fragment is stopped. Not used in this implementation. */
    override fun onFragmentStopped(manager: FragmentManager, fragment: Fragment) {
    }

    /** Called when a fragment's view is created. Not used in this implementation. */
    override fun onFragmentViewCreated(manager: FragmentManager, fragment: Fragment, v: View, state: Bundle?) {
    }

    /** Called when a fragment's view is destroyed. Not used in this implementation. */
    override fun onFragmentViewDestroyed(manager: FragmentManager, fragment: Fragment) {
    }

    /** Called when a fragment is detached from its context. Not used in this implementation. */
    override fun onFragmentDetached(manager: FragmentManager, fragment: Fragment) {
    }

    /** Called when a fragment is destroyed. Not used in this implementation. */
    override fun onFragmentDestroyed(manager: FragmentManager, fragment: Fragment) {
    }
}
