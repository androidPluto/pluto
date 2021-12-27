package com.pluto.modules.exceptions

import androidx.annotation.Keep

@Keep
interface ANRListener {
    fun onAppNotResponding(exception: ANRException)
}
