package com.pluto.applifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.pluto.DebugNotification
import com.pluto.plugins.PluginSelectorActivity

internal class AppLifecycle : ActivityLifecycleCallbacks {

    internal val state: LiveData<AppState>
        get() = _state
    private val _state = MutableLiveData<AppState>()

    private val showingSelector = MutableLiveData<Boolean>()

    internal val shouldShowNotch: LiveData<Boolean>
        get() = _shouldShowNotch
    private val _shouldShowNotch = MediatorLiveData<Boolean>()

    private var activityCount = 0

    init {
        _shouldShowNotch.addSource(showingSelector) {
            _shouldShowNotch.postValue(getState(state.value, showingSelector.value))
        }
        _shouldShowNotch.addSource(state) {
            _shouldShowNotch.postValue(getState(state.value, showingSelector.value))
        }
    }

    override fun onActivityStarted(activity: Activity) {
        activityCount++
        if (activityCount == 1) {
            _state.postValue(AppState.Foreground)
            DebugNotification(activity.applicationContext).add()
        }
        if (activity is PluginSelectorActivity) {
            showingSelector.postValue(true)
        }
    }

    override fun onActivityStopped(activity: Activity) {
        if (activity is PluginSelectorActivity) {
            showingSelector.postValue(false)
        }
        activityCount--
        if (activityCount == 0) {
            _state.postValue(AppState.Background)
            DebugNotification(activity.applicationContext).remove()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    private fun getState(state: AppState?, showing: Boolean?): Boolean {
        val appState = state ?: AppState.Background
        val showingSelector = showing ?: false

        return if (appState is AppState.Background) {
            false
        } else {
            !showingSelector
        }
    }
}
