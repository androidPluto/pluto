package com.pluto.core.notch

import android.view.WindowManager

internal interface OnNotchInteractionListener {
    fun onClick()
    fun onLayoutParamsUpdated(params: WindowManager.LayoutParams)
}
