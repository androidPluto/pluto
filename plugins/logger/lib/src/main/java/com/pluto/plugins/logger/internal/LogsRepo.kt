package com.pluto.plugins.logger.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

internal object LogsRepo {

    internal val logs: LiveData<List<LogData>>
        get() = _logs
    private val _logs = MutableLiveData<List<LogData>>()
    private val logsList = arrayListOf<LogData>()
    private const val MAX_LIMIT = 256

    fun save(level: Level, tag: String, message: String?, tr: Throwable?, ele: StackTraceElement) {
        synchronized(logsList) {
            logsList.add(0, LogData(level, tag, message ?: "", tr, ele))
            val temp = logsList.take(MAX_LIMIT)
            logsList.clear()
            logsList.addAll(temp)
            _logs.postValue(logsList)
        }
    }

    fun saveEvent(
        level: Level,
        tag: String,
        event: String?,
        attributes: HashMap<String, Any?>?,
        ele: StackTraceElement
    ) {
        logsList.add(0, LogData(level, tag, event ?: "", null, ele, attributes))
        val temp = logsList.take(MAX_LIMIT)
        logsList.clear()
        logsList.addAll(temp)
        _logs.postValue(logsList)
    }

    fun deleteAll() {
        logsList.clear()
        _logs.postValue(logsList)
    }
}
