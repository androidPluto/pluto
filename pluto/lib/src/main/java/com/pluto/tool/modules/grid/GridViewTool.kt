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

internal class GridViewTool : PlutoTool("grid") {

    private var gridView: GridView? = null

    override fun getConfig(): ToolConfiguration = ToolConfiguration(
        name = application.getString(R.string.pluto___tool_grid_name),
        icon = R.drawable.pluto___tool_ic_grid_logo,
    )

    override fun onToolInitialised() {
    }

    override fun onToolSelected() {
        toggle()
    }

    override fun onToolUnselected() {
        hideGrid()
    }

    override fun isEnabled(): Boolean = application.applicationContext.canDrawOverlays()

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

    private fun showGrid() {
        if (gridView == null) {
            gridView = GridView(application)
        }
        gridView?.let {
            application.addViewToWindow(it, layoutParams())
        }
    }

    private fun hideGrid() {
        gridView?.parent?.let {
            application.removeViewFromWindow(gridView!!)
            gridView = null
        }
    }

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

    private fun isShowing(view: View) = ViewCompat.isAttachedToWindow(view)
}
