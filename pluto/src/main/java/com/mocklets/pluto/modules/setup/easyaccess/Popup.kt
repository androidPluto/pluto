package com.mocklets.pluto.modules.setup.easyaccess

import android.app.Service
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.canDrawOverlays
import com.mocklets.pluto.core.extensions.toast
import com.mocklets.pluto.ui.PlutoActivity

internal class Popup(context: Context) {

    private var isIntroToastAlreadyShown = false

    private val interactionListener = object : OnPopupInteractionListener {
        override fun onClick() {
            val intent = Intent(context, PlutoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        override fun onLayoutParamsUpdated(params: WindowManager.LayoutParams) {
            popupViewManager.view.parent?.let {
                windowManager.updateViewLayout(popupViewManager.view, params)
            }
        }
    }

    private val popupViewManager: PopupViewManager = PopupViewManager(context, interactionListener)
    private val windowManager: WindowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager

    internal fun add(context: Context) {
        if (context.canDrawOverlays()) {
            if (popupViewManager.view.parent == null) {
                windowManager.addView(popupViewManager.view, popupViewManager.layoutParams)
            }
        } else {
            if (!isIntroToastAlreadyShown) {
                context.toast(context.getString(R.string.pluto___welcome_toast))
                isIntroToastAlreadyShown = true
            }
        }
    }

    internal fun remove() {
        popupViewManager.view.parent?.let { windowManager.removeView(popupViewManager.view) }
    }
}
