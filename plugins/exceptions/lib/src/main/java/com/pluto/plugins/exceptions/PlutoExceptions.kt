package com.pluto.plugins.exceptions

import android.content.Context
import com.pluto.plugins.exceptions.internal.CrashHandler
import com.pluto.plugins.exceptions.internal.Session
import com.pluto.plugins.exceptions.internal.anr.AnrSupervisor

object PlutoExceptions {

    private var crashHandler: CrashHandler? = null
    private var anrHandler: AnrSupervisor? = null
    lateinit var devIdentifier: String
        private set
    internal val session = Session()
    internal lateinit var appPackageName: String

    internal fun initialize(context: Context, identifier: String) {
        appPackageName = context.packageName
        crashHandler = CrashHandler(context)
        anrHandler = AnrSupervisor().apply { start() }
        devIdentifier = identifier
        Thread.setDefaultUncaughtExceptionHandler(crashHandler)
    }

    @Deprecated("global level plugin options are no longer supported", ReplaceWith("setCrashHandler()"))
    fun setExceptionHandler(uncaughtExceptionHandler: Thread.UncaughtExceptionHandler) {
        this.crashHandler?.let {
            it.setExceptionHandler(uncaughtExceptionHandler)
            return
        }
        throw IllegalStateException("UncaughtExceptionHandler cannot be set as Pluto is not initialised.")
    }

    fun setCrashHandler(uncaughtExceptionHandler: Thread.UncaughtExceptionHandler) {
        this.crashHandler?.let {
            it.setExceptionHandler(uncaughtExceptionHandler)
            return
        }
        throw IllegalStateException("UncaughtExceptionHandler cannot be set as Pluto is not initialised.")
    }

    fun setANRHandler(anrHandler: UncaughtANRHandler) {
        this.anrHandler?.let {
            it.setListener(anrHandler)
            return
        }
        throw IllegalStateException("UncaughtANRHandler cannot be set as Pluto is not initialised.")
    }

    internal fun clear() {
        ExceptionDBHandler.flush()
    }
}
