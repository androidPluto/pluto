package com.mocklets.pluto.modules.exceptions.crashes

import android.content.Context
import com.google.gson.Gson
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.core.DeviceFingerPrint
import com.mocklets.pluto.modules.exceptions.ExceptionAllData
import com.mocklets.pluto.modules.exceptions.asExceptionData
import com.mocklets.pluto.modules.exceptions.asThreadData
import com.mocklets.pluto.modules.exceptions.dao.ExceptionEntity

internal class CrashHandler(context: Context) : Thread.UncaughtExceptionHandler {

    private var handler: Thread.UncaughtExceptionHandler? = Thread.getDefaultUncaughtExceptionHandler()
    private val deviceFingerPrint = DeviceFingerPrint(context)
    private val preferences by lazy { Pluto.preferences }
    private val crashNotification: CrashNotification = CrashNotification(context)

    override fun uncaughtException(t: Thread, e: Throwable) {
        val exceptionData = ExceptionEntity(
            timestamp = System.currentTimeMillis(),
            data = ExceptionAllData(
                thread = t.asThreadData(),
                exception = e.asExceptionData(),
                device = deviceFingerPrint
            )
        )
        preferences.lastSessionCrash = Gson().toJson(exceptionData)
        crashNotification.add()
        handler?.uncaughtException(t, e)
    }

    internal fun setExceptionHandler(handler: Thread.UncaughtExceptionHandler) {
        this.handler = handler
    }
}
