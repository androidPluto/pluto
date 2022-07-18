package com.pluto.plugins.exceptions

import android.content.Context
import com.pluto.plugin.utilities.extensions.capitalizeText
import com.pluto.plugins.exceptions.internal.CrashHandler
import com.pluto.plugins.exceptions.internal.Preferences
import com.pluto.plugins.exceptions.internal.Session
import com.pluto.plugins.exceptions.internal.anr.AnrSupervisor
import com.pluto.plugins.exceptions.internal.dao.ExceptionDao
import com.pluto.plugins.exceptions.internal.dao.ExceptionEntity
import com.pluto.plugins.exceptions.internal.dao.database.DatabaseManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object PlutoExceptions {

    private var crashHandler: CrashHandler? = null
    private var anrHandler: AnrSupervisor? = null
    lateinit var devIdentifier: String
        private set
    internal val session = Session()
    internal lateinit var appPackageName: String
    private lateinit var exceptionDao: ExceptionDao
    private var moshi = Moshi.Builder().build()
    private var moshiAdapter: JsonAdapter<ExceptionEntity> = moshi.adapter(ExceptionEntity::class.java)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    internal fun initialize(context: Context, identifier: String) {
        appPackageName = context.packageName
        crashHandler = CrashHandler(context)
        anrHandler = AnrSupervisor().apply { start() }
        devIdentifier = identifier
        checkAndSaveCrash(context)
        Thread.setDefaultUncaughtExceptionHandler(crashHandler)
        exceptionDao = DatabaseManager(context).db.exceptionDao()
    }

    private fun checkAndSaveCrash(context: Context) {
        val preferences = Preferences(context)
        preferences.lastSessionCrash?.let {
            moshiAdapter.fromJson(it)?.let { exception ->
                coroutineScope.launch {
                    DatabaseManager(context).db.exceptionDao().save(
                        ExceptionEntity(
                            timestamp = exception.timestamp,
                            data = exception.data
                        )
                    )
                }
            }
        }
        preferences.lastSessionCrash = null
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

    @SuppressWarnings("UnusedPrivateMember", "EmptyFunctionBlock")
    fun setANRHandler(anrHandler: UncaughtANRHandler) {
        this.anrHandler?.let {
            it.setListener(anrHandler)
            return
        }
        throw IllegalStateException("UncaughtANRHandler cannot be set as Pluto is not initialised.")
    }

    fun getPriorityString(priority: Int) =
        when (priority) {
            Thread.MAX_PRIORITY -> "maximum"
            Thread.MIN_PRIORITY -> "minimum"
            else -> "normal"
        }.capitalizeText()

    fun clear() {
        coroutineScope.launch {
            exceptionDao.deleteAll()
        }
    }
}
