package com.pluto.plugins.logger.internal.persistence

import android.content.Context
import com.pluto.plugins.logger.internal.Level
import com.pluto.plugins.logger.internal.LogData
import com.pluto.plugins.logger.internal.StackTrace
import com.pluto.plugins.logger.internal.asExceptionData
import com.pluto.plugins.logger.internal.persistence.database.DatabaseManager
import java.util.HashMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal object LogDBHandler {

    private lateinit var logDao: LogDao
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun initialize(context: Context) {
        logDao = DatabaseManager(context).db.exceptionDao()
    }

    fun persist(level: Level, tag: String, message: String?, tr: Throwable?, ele: StackTrace) {
        coroutineScope.launch {
            logDao.save(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    data = LogData(level, tag, message ?: "", tr?.asExceptionData(), ele)
                ),
            )
        }
    }

    fun persist(level: Level, tag: String, event: String, attr: HashMap<String, Any?>?, ele: StackTrace) {
        coroutineScope.launch {
            logDao.save(
                LogEntity(
                    timestamp = System.currentTimeMillis(),
                    data = LogData(level, tag, event ?: "", null, ele, attr)
                ),
            )
        }
    }

    suspend fun fetchAll(): List<LogEntity>? {
        return logDao.fetchAll()
    }

    fun flush() {
        coroutineScope.launch {
            logDao.deleteAll()
        }
    }
}
