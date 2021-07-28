package com.mocklets.pluto.modules.exceptions

import android.content.Context
import com.google.gson.Gson
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.core.DeviceFingerPrint
import com.mocklets.pluto.core.database.DatabaseManager
import com.mocklets.pluto.core.extensions.capitalizeText
import com.mocklets.pluto.modules.exceptions.dao.ExceptionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class ExceptionRepo(context: Context) {

    private val db = DatabaseManager(context).db
    private val preferences by lazy { Pluto.preferences }
    private val deviceFingerPrint = DeviceFingerPrint(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        checkAndSaveCrash()
    }

    @Synchronized
    private fun checkAndSaveCrash() {
        scope.launch {
            preferences.lastSessionCrash?.let {
                val exception = Gson().fromJson(it, ExceptionEntity::class.java)
                db.exceptionDao().save(
                    ExceptionEntity(
                        timestamp = exception.timestamp,
                        data = exception.data
                    )
                )
                preferences.lastSessionCrash = null
            }
        }
    }

    @Synchronized
    internal fun saveANR(e: ANRException) {
        scope.launch {
            db.exceptionDao().save(
                ExceptionEntity(
                    timestamp = System.currentTimeMillis(),
                    data = ExceptionAllData(
                        exception = e.asExceptionData(true),
                        device = deviceFingerPrint,
                        threadStateList = ThreadStates(e.threadStateList)
                    )
                )
            )
        }
    }

    companion object {
        fun getPriorityString(priority: Int) =
            when (priority) {
                Thread.MAX_PRIORITY -> "maximum"
                Thread.MIN_PRIORITY -> "minimum"
                else -> "normal"
            }.capitalizeText()
    }
}
