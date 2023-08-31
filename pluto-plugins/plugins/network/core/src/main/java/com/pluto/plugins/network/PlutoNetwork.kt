package com.pluto.plugins.network

import android.content.Context

object PlutoNetwork {

    var applicationContext: Context? = null
        private set

    internal fun initialize(context: Context) {
        applicationContext = context.applicationContext
    }
}
