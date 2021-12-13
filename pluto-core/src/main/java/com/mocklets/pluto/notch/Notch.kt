package com.mocklets.pluto.notch

import android.app.Service
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import com.mocklets.pluto.settings.canDrawOverlays
import com.mocklets.pluto.ui.PlutoActivity

internal class Notch(context: Context) {

    private val interactionListener = object : OnNotchInteractionListener {
        override fun onClick() {
            val intent = Intent(context, PlutoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        override fun onLayoutParamsUpdated(params: WindowManager.LayoutParams) {
            notchViewManager.view?.parent?.let {
                windowManager.updateViewLayout(notchViewManager.view, params)
            }
        }
    }

    private val notchViewManager: NotchViewManager = NotchViewManager(context, interactionListener)
    private val windowManager: WindowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager

    internal fun add(context: Context) {
        if (context.canDrawOverlays()) {
            notchViewManager.addView(context, windowManager)
        }
    }

    internal fun remove() {
        notchViewManager.removeView(windowManager)
    }
}
