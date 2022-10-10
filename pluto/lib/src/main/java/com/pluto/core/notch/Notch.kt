package com.pluto.core.notch

import android.app.Application
import android.app.Service
import android.view.WindowManager
import androidx.lifecycle.LiveData
import com.pluto.Pluto
import com.pluto.core.applifecycle.AppState
import com.pluto.utilities.extensions.canDrawOverlays

internal class Notch(private val application: Application, shouldShowNotch: LiveData<Boolean>) {

    init {
        shouldShowNotch.observeForever {
            if (it) {
                add()
            } else {
                remove()
            }
        }
    }

    private val interactionListener = object : OnNotchInteractionListener {
        override fun onClick() {
            Pluto.open()
        }

        override fun onLayoutParamsUpdated(params: WindowManager.LayoutParams) {
            notchViewManager.view?.parent?.let {
                windowManager.updateViewLayout(notchViewManager.view, params)
            }
        }
    }

    private var enabled = true
    private val notchViewManager: NotchViewManager = NotchViewManager(application.applicationContext, interactionListener)
    private val windowManager: WindowManager = application.applicationContext.getSystemService(Service.WINDOW_SERVICE) as WindowManager

    private fun add() {
        if (enabled) {
            val context = application.applicationContext
            if (context.canDrawOverlays()) {
                notchViewManager.addView(context, windowManager)
            }
        }
    }

    private fun remove() {
        notchViewManager.removeView(windowManager)
    }

    internal fun enable(state: Boolean) {
        enabled = state
        if (enabled && Pluto.appStateCallback.state.value is AppState.Foreground) {
            add()
        } else {
            remove()
        }
    }
}
