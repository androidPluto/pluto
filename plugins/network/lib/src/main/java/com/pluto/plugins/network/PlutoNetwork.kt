package com.pluto.plugins.network

import android.content.Context
import com.pluto.plugins.network.internal.interceptor.logic.core.CacheDirectoryProvider

internal object PlutoNetwork {
    var cacheDirectoryProvider: CacheDirectoryProvider? = null
        private set

    fun initialize(context: Context) {
        cacheDirectoryProvider = CacheDirectoryProvider { context.applicationContext.filesDir }
    }
}
