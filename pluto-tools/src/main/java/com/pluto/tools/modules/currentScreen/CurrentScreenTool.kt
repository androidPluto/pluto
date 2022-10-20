package com.pluto.tools.modules.currentScreen

import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import com.pluto.tools.PlutoTool
import com.pluto.tools.R
import com.pluto.tools.ToolConfiguration
import com.pluto.utilities.extensions.addViewToWindow
import com.pluto.utilities.extensions.canDrawOverlays
import com.pluto.utilities.extensions.removeViewFromWindow

internal class CurrentScreenTool : PlutoTool("currentScreen") {

    private var gridView: CurrentScreenView? = null

    private val onCurrentViewUpdateListener = object : OnCurrentScreenUpdateListener {
        override fun onUpdate(fragment: String?, activity: String?) {
            gridView?.updateText(activity, fragment)
        }
    }

    override fun getConfig(): ToolConfiguration = ToolConfiguration(
        name = application.getString(R.string.pluto_tool___current_screen_name),
        icon = R.drawable.pluto_tool___ic_current_screen_logo,
    )

    override fun onToolInitialised() {
        application.registerActivityLifecycleCallbacks(AppLifecycleListener(onCurrentViewUpdateListener))
    }

    override fun onToolSelected() {
        toggle()
    }

    override fun onToolUnselected() {
        hideView()
    }

    override fun isEnabled(): Boolean = application.applicationContext.canDrawOverlays()

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

    private fun showView() {
        if (gridView == null) {
            gridView = CurrentScreenView(application)
        }
        gridView?.let {
            application.addViewToWindow(it, layoutParams())
        }
    }

    private fun hideView() {
        gridView?.parent?.let {
            application.removeViewFromWindow(gridView!!)
            gridView = null
        }
    }

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

    private fun isShowing(view: View) = ViewCompat.isAttachedToWindow(view)
}
