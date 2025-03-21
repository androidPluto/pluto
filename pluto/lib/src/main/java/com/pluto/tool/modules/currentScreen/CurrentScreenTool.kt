package com.pluto.tool.modules.currentScreen

import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import com.pluto.R
import com.pluto.tool.PlutoTool
import com.pluto.tool.ToolConfiguration
import com.pluto.utilities.extensions.addViewToWindow
import com.pluto.utilities.extensions.canDrawOverlays
import com.pluto.utilities.extensions.removeViewFromWindow

/**
 * Tool that displays the current activity and fragment names on screen.
 *
 * This tool shows an overlay at the bottom of the screen that displays
 * the name of the currently visible activity and fragment. It's useful
 * for developers to quickly identify which screen they're looking at.
 */
internal class CurrentScreenTool : PlutoTool("currentScreen") {

    /** The view that displays the current screen information */
    private var gridView: CurrentScreenView? = null

    /**
     * Listener that receives updates when the current activity or fragment changes.
     *
     * This listener updates the displayed text in the overlay view.
     */
    private val onCurrentViewUpdateListener = object : OnCurrentScreenUpdateListener {
        /**
         * Called when the current activity or fragment changes.
         *
         * @param fragment The name of the current fragment, or null if none
         * @param activity The name of the current activity, or null if none
         */
        override fun onUpdate(fragment: String?, activity: String?) {
            gridView?.updateText(activity, fragment)
        }
    }

    /**
     * Returns the configuration for this tool.
     *
     * @return The tool configuration with name and icon
     */
    override fun getConfig(): ToolConfiguration = ToolConfiguration(
        name = application.getString(R.string.pluto___tool_current_screen_name),
        icon = R.drawable.pluto___tool_ic_current_screen_logo,
    )

    /**
     * Called when the tool is initialized.
     *
     * Registers activity lifecycle callbacks to track activity and fragment changes.
     */
    override fun onToolInitialised() {
        application.registerActivityLifecycleCallbacks(AppLifecycleListener(onCurrentViewUpdateListener))
    }

    /**
     * Called when the tool is selected.
     *
     * Toggles the visibility of the current screen overlay.
     */
    override fun onToolSelected() {
        toggle()
    }

    /**
     * Called when the tool is unselected.
     *
     * Hides the current screen overlay.
     */
    override fun onToolUnselected() {
        hideView()
    }

    /**
     * Determines whether the tool is enabled.
     *
     * The tool is enabled if the app has permission to draw overlays.
     *
     * @return True if the tool is enabled, false otherwise
     */
    override fun isEnabled(): Boolean = application.applicationContext.canDrawOverlays()

    /**
     * Toggles the visibility of the current screen overlay.
     *
     * If the overlay is visible, it will be hidden.
     * If the overlay is hidden, it will be shown.
     */
    private fun toggle() {
        gridView?.let {
            if (isShowing(it)) {
                hideView()
            } else {
                showView()
            }
        } ?: run {
            showView()
        }
    }

    /**
     * Shows the current screen overlay.
     *
     * Creates the overlay view if it doesn't exist and adds it to the window.
     */
    private fun showView() {
        if (gridView == null) {
            gridView = CurrentScreenView(application)
        }
        gridView?.let {
            application.addViewToWindow(it, layoutParams())
        }
    }

    /**
     * Hides the current screen overlay.
     *
     * Removes the overlay view from the window and nullifies the reference.
     */
    private fun hideView() {
        gridView?.parent?.let {
            application.removeViewFromWindow(gridView!!)
            gridView = null
        }
    }

    /**
     * Creates layout parameters for the overlay view.
     *
     * @return The WindowManager.LayoutParams for the overlay view
     */
    private fun layoutParams(): WindowManager.LayoutParams {
        val params = WindowManager.LayoutParams()
        params.width = FrameLayout.LayoutParams.MATCH_PARENT
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        } else {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        params.format = PixelFormat.TRANSLUCENT
        params.gravity = Gravity.START or Gravity.BOTTOM
        return params
    }

    /**
     * Determines whether the view is currently showing.
     *
     * @param view The view to check
     * @return True if the view is attached to the window, false otherwise
     */
    private fun isShowing(view: View) = ViewCompat.isAttachedToWindow(view)
}
