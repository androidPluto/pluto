package com.pluto.tools.modules.currentScreen

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

internal class FragmentLifecycleListener(private val screenUpdateCallback: OnCurrentScreenUpdateListener) : FragmentManager.FragmentLifecycleCallbacks() {
    var activity: Activity? = null

    override fun onFragmentCreated(manager: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {
    }

    override fun onFragmentAttached(manager: FragmentManager, fragment: Fragment, context: Context) {
    }

    override fun onFragmentStarted(manager: FragmentManager, fragment: Fragment) {
    }

    override fun onFragmentResumed(manager: FragmentManager, fragment: Fragment) {
        screenUpdateCallback.onUpdate(fragment::class.java.name, activity?.let { it::class.java.name } ?: run { null })
    }

    override fun onFragmentPaused(manager: FragmentManager, fragment: Fragment) {
    }

    override fun onFragmentStopped(manager: FragmentManager, fragment: Fragment) {
    }

    override fun onFragmentViewCreated(manager: FragmentManager, fragment: Fragment, v: View, state: Bundle?) {
    }

    override fun onFragmentViewDestroyed(manager: FragmentManager, fragment: Fragment) {
    }

    override fun onFragmentDetached(manager: FragmentManager, fragment: Fragment) {
    }

    override fun onFragmentDestroyed(manager: FragmentManager, fragment: Fragment) {
    }
}
