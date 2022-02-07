package com.mocklets.pluto

import android.content.Context
import androidx.annotation.Keep
import com.mocklets.pluto.modules.exceptions.ANRListener

@Keep
object Pluto {

    @JvmOverloads
    fun initialize(context: Context, shouldShowIntroToast: Boolean = true) {}

    fun setAppProperties(properties: HashMap<String, String?>) {}

    fun setExceptionHandler(uncaughtExceptionHandler: Thread.UncaughtExceptionHandler) {}

    fun setANRListener(listener: ANRListener) {}

    fun showUi() {}
}
