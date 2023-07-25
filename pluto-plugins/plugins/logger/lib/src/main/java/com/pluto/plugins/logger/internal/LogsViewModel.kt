package com.pluto.plugins.logger.internal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pluto.plugins.logger.internal.persistence.LogDBHandler
import com.pluto.plugins.logger.internal.persistence.LogEntity
import com.pluto.utilities.extensions.asFormattedDate
import com.pluto.utilities.list.ListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class LogsViewModel(application: Application) : AndroidViewModel(application) {

    private var rawLogs: List<LogEntity>? = null
    val logs: LiveData<List<ListItem>>
        get() = _logs
    private val _logs = MutableLiveData<List<ListItem>>()

    val current: LiveData<LogData>
        get() = _current
    private val _current = MutableLiveData<LogData>()

    val serializedLogs: LiveData<String>
        get() = _serializedLogs
    private val _serializedLogs = MutableLiveData<String>()

    fun fetch(search: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            if (rawLogs == null) {
                rawLogs = LogDBHandler.fetchAll()
            }
            val currentSessionLogs = (rawLogs ?: arrayListOf()).filter { it.sessionId == Session.id && it.data.isValidSearch(search) }.map { it.data }
            val previousSessionLogs = (rawLogs ?: arrayListOf()).filter { it.sessionId != Session.id && it.data.isValidSearch(search) }.map { it.data }

            val list = arrayListOf<ListItem>()
            list.addAll(currentSessionLogs)
            if (previousSessionLogs.isNotEmpty()) {
                list.add(LogPreviousSessionHeader())
                list.addAll(previousSessionLogs)
            }
            _logs.postValue(list)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            LogDBHandler.flush()
            _logs.postValue(arrayListOf())
        }
    }

    internal fun updateCurrentLog(data: LogData) {
        _current.postValue(data)
    }

    internal fun serializeLogs() {
        viewModelScope.launch {
            val text = StringBuilder()
            text.append("Pluto Log Trace")
            logs.value?.forEach {
                if (it is LogData) {
                    text.append("\n----------\n")
                    text.append("${it.timeStamp.asFormattedDate(DATE_FORMAT)} ${it.level.label.uppercase()} | ${it.tag}: ${it.message}")
                    it.tr?.let { tr ->
                        text.append("\n\tException: ${tr}\n")
                        tr.stackTrace.take(MAX_STACK_TRACE_LINES).forEach { trace ->
                            text.append("\t\t at $trace\n")
                        }
                        if (tr.stackTrace.size - MAX_STACK_TRACE_LINES > 0) {
                            text.append("\t\t + ${tr.stackTrace.size - MAX_STACK_TRACE_LINES} more lines")
                        }
                    }
                    it.eventAttributes?.let { attr ->
                        if (attr.isNotEmpty()) {
                            text.append("\n\tEvent Attributes (${attr.size}):")
                            attr.forEach { entry ->
                                text.append("\n\t\t${entry.key}: ${entry.value}")
                            }
                        }
                    }
                }
            }
            _serializedLogs.postValue(text.toString())
        }
    }

    companion object {
        const val MAX_STACK_TRACE_LINES = 15
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"
    }
}

private fun LogData.isValidSearch(search: String): Boolean =
    search.isEmpty() || tag.contains(search, true) || message.contains(search, true) || stackTrace.fileName.contains(search, true)
