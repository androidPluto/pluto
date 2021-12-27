package com.pluto.notch

import android.app.Application
import android.app.Service
import android.view.WindowManager
import com.pluto.Pluto
import com.pluto.applifecycle.AppState
import com.pluto.settings.canDrawOverlays

internal class Notch(private val application: Application) {

    init {
        Pluto.appState.observeForever {
            if (enabled && it is AppState.Foreground) {
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

    private var enabled = false
    private val notchViewManager: NotchViewManager = NotchViewManager(application.applicationContext, interactionListener)
    private val windowManager: WindowManager = application.applicationContext.getSystemService(Service.WINDOW_SERVICE) as WindowManager

    internal fun add() {
        if (enabled) {
            val context = application.applicationContext
            if (context.canDrawOverlays()) {
                notchViewManager.addView(context, windowManager)
            } else {
                // todo show instruction dialog
            }
        }
    }

    internal fun remove() {
        notchViewManager.removeView(windowManager)
    }

    internal fun enable(state: Boolean) {
        enabled = state
        if (enabled && Pluto.appState.value is AppState.Foreground) {
            add()
        } else {
            remove()
        }
    }
}
