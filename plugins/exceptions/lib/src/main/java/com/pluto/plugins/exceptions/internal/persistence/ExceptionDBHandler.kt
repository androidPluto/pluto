package com.pluto.plugins.exceptions.internal.persistence

import android.content.Context
import androidx.annotation.Keep
import com.pluto.plugin.utilities.device.Device
import com.pluto.plugins.exceptions.internal.DeviceInfo
import com.pluto.plugins.exceptions.internal.ExceptionAllData
import com.pluto.plugins.exceptions.internal.asDeviceInfo
import com.pluto.plugins.exceptions.internal.asExceptionData
import com.pluto.plugins.exceptions.internal.asThreadData
import com.pluto.plugins.exceptions.internal.persistence.database.DatabaseManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal object ExceptionDBHandler {

    private lateinit var exceptionDao: ExceptionDao
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private lateinit var deviceInfo: DeviceInfo
    private lateinit var preferences: Preferences
    private var moshi = Moshi.Builder().build()
    private var moshiAdapter: JsonAdapter<ExceptionTempData> = moshi.adapter(ExceptionTempData::class.java)

    fun initialize(context: Context) {
        deviceInfo = Device(context).asDeviceInfo()
        exceptionDao = DatabaseManager(context).db.exceptionDao()
        preferences = Preferences(context)
        checkAndSaveCrash()
    }

    private fun checkAndSaveCrash() {
        preferences.lastSessionCrash?.let {
            moshiAdapter.fromJson(it)?.let { exception ->
                persist(exception.exception, exception.timestamp) {
                    preferences.lastSessionCrash = null
                }
            }
        }
    }

    fun persist(exception: ExceptionAllData, timestamp: Long, onSuccess: (() -> Unit)? = null) {
        coroutineScope.launch {
            exceptionDao.save(
                ExceptionEntity(
                    timestamp = timestamp,
                    data = exception,
                    device = deviceInfo
                )
            )
            onSuccess?.invoke()
        }
    }

    fun flush() {
        coroutineScope.launch {
            exceptionDao.deleteAll()
        }
    }

    fun tempPersist(exception: Throwable, thread: Thread) {
        val exceptionData = ExceptionTempData(
            exception = ExceptionAllData(
                thread = thread.asThreadData(),
                exception = exception.asExceptionData()
            ),
            timestamp = System.currentTimeMillis()
        )
        preferences.lastSessionCrash = moshiAdapter.toJson(exceptionData)
    }
}

@Keep
@JsonClass(generateAdapter = true)
internal data class ExceptionTempData(
    val timestamp: Long,
    val exception: ExceptionAllData
)
