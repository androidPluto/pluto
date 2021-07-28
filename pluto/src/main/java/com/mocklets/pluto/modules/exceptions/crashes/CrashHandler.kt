package com.mocklets.pluto.modules.exceptions.crashes

import android.content.Context
import com.google.gson.Gson
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.core.DeviceFingerPrint
import com.mocklets.pluto.modules.exceptions.ExceptionAllData
import com.mocklets.pluto.modules.exceptions.asExceptionData
import com.mocklets.pluto.modules.exceptions.asThreadData
import com.mocklets.pluto.modules.exceptions.dao.ExceptionEntity

internal class CrashHandler(private val context: Context) : Thread.UncaughtExceptionHandler {

    private val defaultUEH = Thread.getDefaultUncaughtExceptionHandler()
    private val deviceFingerPrint = DeviceFingerPrint(context)
    private val preferences by lazy { Pluto.preferences }

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
        CrashNotification(context).add()
        defaultUEH?.uncaughtException(t, e)
    }
}
