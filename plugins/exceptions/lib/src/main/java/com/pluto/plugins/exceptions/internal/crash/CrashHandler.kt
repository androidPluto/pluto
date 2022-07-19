package com.pluto.plugins.exceptions.internal.crash

import android.content.Context
import com.pluto.plugins.exceptions.internal.persistence.ExceptionDBHandler

internal class CrashHandler(context: Context) : Thread.UncaughtExceptionHandler {

    private var handler: Thread.UncaughtExceptionHandler? = Thread.getDefaultUncaughtExceptionHandler()
    private val crashNotification: CrashNotification = CrashNotification(context)

    override fun uncaughtException(t: Thread, e: Throwable) {
        ExceptionDBHandler.tempPersist(e, t)
        crashNotification.add()
        handler?.uncaughtException(t, e)
    }

    internal fun setExceptionHandler(handler: Thread.UncaughtExceptionHandler) {
        this.handler = handler
    }
}
