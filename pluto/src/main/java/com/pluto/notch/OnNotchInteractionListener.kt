package com.pluto.notch

import android.view.WindowManager

internal interface OnNotchInteractionListener {
    fun onClick()
    fun onLayoutParamsUpdated(params: WindowManager.LayoutParams)
}
