package com.pluto.tools.modules.grid

import android.graphics.PixelFormat
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import com.pluto.tools.PlutoTool
import com.pluto.tools.R
import com.pluto.tools.ToolConfiguration
import com.pluto.utilities.extensions.addViewToWindow
import com.pluto.utilities.extensions.removeViewFromWindow

internal class PlutoGridTool : PlutoTool("grid") {

    private var gridView: GridView? = null

    override fun getConfig(): ToolConfiguration = ToolConfiguration(
        name = context.getString(R.string.pluto_tool___grid_name),
        icon = R.drawable.pluto_tool___ic_grid_logo,
    )

    override fun onToolInitialised() {
    }

    override fun onToolSelected() {
        toggle()
    }

    override fun onToolUnselected() {
        hideGrid()
    }

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
            gridView = GridView(context.applicationContext)
        }
        gridView?.let {
            context.addViewToWindow(it, layoutParams())
        }
    }

    private fun hideGrid() {
        gridView?.parent?.let {
            context.removeViewFromWindow(gridView!!)
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
