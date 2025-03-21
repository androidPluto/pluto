package com.pluto.core.notch

import android.app.Application
import android.app.Service
import android.view.WindowManager
import androidx.lifecycle.LiveData
import com.pluto.Pluto
import com.pluto.core.applifecycle.AppStateCallback
import com.pluto.utilities.extensions.canDrawOverlays

/**
 * Manages the floating notch UI component that provides quick access to Pluto.
 *
 * The notch is a small floating button that appears on top of the application UI
 * and allows users to quickly open Pluto's debugging interface. It observes state
 * changes to determine when it should be shown or hidden.
 *
 * @property application The application instance used for context
 * @param shouldShowNotch LiveData that determines whether the notch should be visible
 */
internal class Notch(private val application: Application, shouldShowNotch: LiveData<Boolean>) {

    /**
     * Initializes the notch by observing the shouldShowNotch LiveData.
     * When the value changes, the notch is either added or removed accordingly.
     */
    init {
        shouldShowNotch.observeForever {
            if (it) {
                add()
            } else {
                remove()
            }
        }
    }

    /**
     * Listener for notch interaction events.
     * Handles click events and layout parameter updates.
     */
    private val interactionListener = object : OnNotchInteractionListener {
        /**
         * Called when the notch is clicked.
         * Opens the Pluto debugging interface.
         */
        override fun onClick() {
            Pluto.open()
        }

        /**
         * Called when the notch's layout parameters are updated.
         * Updates the notch's position in the window.
         *
         * @param params The updated window layout parameters
         */
        override fun onLayoutParamsUpdated(params: WindowManager.LayoutParams) {
            notchViewManager.view?.parent?.let {
                windowManager.updateViewLayout(notchViewManager.view, params)
            }
        }
    }

    /** Flag indicating whether the notch is enabled */
    private var enabled = true

    /** Manages the notch view creation and lifecycle */
    private val notchViewManager: NotchViewManager = NotchViewManager(application.applicationContext, interactionListener)

    /** Window manager used to add and remove the notch view */
    private val windowManager: WindowManager = application.applicationContext.getSystemService(Service.WINDOW_SERVICE) as WindowManager

    /**
     * Adds the notch to the window if enabled and permission is granted.
     */
    private fun add() {
        if (enabled) {
            val context = application.applicationContext
            if (context.canDrawOverlays()) {
                notchViewManager.addView(context, windowManager)
            }
        }
    }

    /**
     * Removes the notch from the window.
     */
    private fun remove() {
        notchViewManager.removeView(windowManager)
    }

    /**
     * Enables or disables the notch.
     *
     * When enabled and the app is in the foreground, the notch will be shown.
     * When disabled or the app is in the background, the notch will be hidden.
     *
     * @param state True to enable the notch, false to disable it
     */
    internal fun enable(state: Boolean) {
        enabled = state
        if (enabled && Pluto.appStateCallback.state.value is AppStateCallback.State.Foreground) {
            add()
        } else {
            remove()
        }
    }
}
