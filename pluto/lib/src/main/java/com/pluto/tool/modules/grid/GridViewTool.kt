package com.pluto.tool.modules.grid

import android.graphics.PixelFormat
import android.os.Build
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
 * A tool that displays a grid overlay on the screen to help with UI alignment and measurement.
 *
 * This tool creates a transparent overlay with a grid pattern that can be toggled on and off.
 * The grid helps developers visualize layout alignment, spacing, and proportions during app development.
 * It requires the SYSTEM_ALERT_WINDOW permission (draw over other apps) to function.
 */
internal class GridViewTool : PlutoTool("grid") {

    private var gridView: GridView? = null

    /**
     * Provides the configuration for this tool, including its name and icon.
     *
     * @return A ToolConfiguration object with the tool's display properties
     */
    override fun getConfig(): ToolConfiguration = ToolConfiguration(
        name = application.getString(R.string.pluto___tool_grid_name),
        icon = R.drawable.pluto___tool_ic_grid_logo,
    )

    /**
     * Called when the tool is initialized.
     * No specific initialization is needed for this tool.
     */
    override fun onToolInitialised() {
    }

    /**
     * Called when the tool is selected by the user.
     * Toggles the grid visibility.
     */
    override fun onToolSelected() {
        toggle()
    }

    /**
     * Called when the tool is unselected by the user.
     * Hides the grid if it's currently visible.
     */
    override fun onToolUnselected() {
        hideGrid()
    }

    /**
     * Determines if this tool is enabled based on whether the app has permission to draw over other apps.
     *
     * @return true if the app has the SYSTEM_ALERT_WINDOW permission, false otherwise
     */
    override fun isEnabled(): Boolean = application.applicationContext.canDrawOverlays()

    /**
     * Toggles the grid visibility - shows it if it's hidden, hides it if it's showing.
     */
    private fun toggle() {
        gridView?.let {
            if (isShowing(it)) {
                hideGrid()
            } else {
                showGrid()
            }
        } ?: run {
            showGrid()
        }
    }

    /**
     * Shows the grid overlay on the screen.
     * Creates a new GridView if one doesn't exist and adds it to the window.
     */
    private fun showGrid() {
        if (gridView == null) {
            gridView = GridView(application)
        }
        gridView?.let {
            application.addViewToWindow(it, layoutParams())
        }
    }

    /**
     * Hides the grid overlay if it's currently visible.
     * Removes the GridView from the window and nullifies the reference.
     */
    private fun hideGrid() {
        gridView?.parent?.let {
            application.removeViewFromWindow(gridView!!)
            gridView = null
        }
    }

    /**
     * Creates the layout parameters for the grid overlay window.
     * Sets up a full-screen, non-touchable, translucent overlay.
     *
     * @return WindowManager.LayoutParams configured for the grid overlay
     */
    private fun layoutParams(): WindowManager.LayoutParams {
        val params = WindowManager.LayoutParams()
        params.width = FrameLayout.LayoutParams.MATCH_PARENT
        params.height = FrameLayout.LayoutParams.MATCH_PARENT
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        } else {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        params.format = PixelFormat.TRANSLUCENT
        return params
    }

    /**
     * Checks if the given view is currently attached to a window (visible).
     *
     * @param view The view to check
     * @return true if the view is attached to a window, false otherwise
     */
    private fun isShowing(view: View) = ViewCompat.isAttachedToWindow(view)
}
