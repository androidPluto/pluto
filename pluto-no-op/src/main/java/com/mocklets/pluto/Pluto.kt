package com.mocklets.pluto

import android.content.Context
import androidx.annotation.Keep
import com.mocklets.pluto.modules.exceptions.ANRListener

@Keep
object Pluto {

    fun initialize(context: Context) {}

    fun setAppProperties(properties: HashMap<String, String?>) {}

    fun setANRListener(listener: ANRListener) {}
}
