package com.mocklets.pluto.modules.setup.easyaccess

import android.view.WindowManager

internal interface OnPopupInteractionListener {
    fun onClick()
    fun onLayoutParamsUpdated(params: WindowManager.LayoutParams)
}
