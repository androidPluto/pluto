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

    private var logDao: LogDao? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    /**
     * Store log temporarily till the LogDBHandler is not initialised.
     */
    private val tempList = arrayListOf<LogEntity>()

    fun initialize(context: Context) {
        logDao = DatabaseManager(context).db.exceptionDao()
        processTempList()
    }

    fun persistLog(level: Level, tag: String, message: String?, tr: Throwable?, ele: StackTrace) {
        coroutineScope.launch {
            val logEntity = LogEntity(data = LogData(level, tag, message ?: "", tr?.asExceptionData(), ele))
            logDao?.save(logEntity) ?: run { pushToTempList(logEntity) }
        }
    }

    fun persistEvent(level: Level, tag: String, event: String, attr: HashMap<String, Any?>?, ele: StackTrace) {
        coroutineScope.launch {
            val logEntity = LogEntity(data = LogData(level, tag, event, null, ele, attr))
            logDao?.save(logEntity) ?: run { pushToTempList(logEntity) }
        }
    }

    suspend fun fetchAll(): List<LogEntity>? {
        return logDao?.fetchAll()
    }

    fun flush() {
        coroutineScope.launch {
            logDao?.deleteAll()
        }
    }

    private fun pushToTempList(logEntity: LogEntity) {
        tempList.add(logEntity)
    }

    private fun processTempList() {
        coroutineScope.launch {
            logDao?.saveAll(tempList)
            tempList.clear()
        }
    }
}
