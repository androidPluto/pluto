package com.pluto.plugins.exceptions.internal

import android.content.Context
import com.pluto.plugin.utilities.device.Device
import com.pluto.plugins.exceptions.internal.dao.ExceptionEntity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

internal class CrashHandler(context: Context) : Thread.UncaughtExceptionHandler {

    private var handler: Thread.UncaughtExceptionHandler? = Thread.getDefaultUncaughtExceptionHandler()
    private val device = Device(context)
    private val preferences by lazy { Preferences(context) }
    private val crashNotification: CrashNotification = CrashNotification(context)
    private var moshi = Moshi.Builder().build()
    private var moshiAdapter: JsonAdapter<ExceptionEntity> = moshi.adapter(ExceptionEntity::class.java)

    override fun uncaughtException(t: Thread, e: Throwable) {
        val exceptionData = ExceptionEntity(
            timestamp = System.currentTimeMillis(),
            data = ExceptionAllData(
                thread = t.asThreadData(),
                exception = e.asExceptionData(),
                device = device.asDeviceInfo()
            )
        )
        preferences.lastSessionCrash = moshiAdapter.toJson(exceptionData)
        crashNotification.add()
        handler?.uncaughtException(t, e)
    }

    internal fun setExceptionHandler(handler: Thread.UncaughtExceptionHandler) {
        this.handler = handler
    }
}
