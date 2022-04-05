package com.pluto.plugins.logger.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluto.plugin.utilities.extensions.asFormattedDate
import kotlinx.coroutines.launch

internal class LogsViewModel : ViewModel() {
    val logs: LiveData<List<LogData>>
        get() = LogsRepo.logs

    val current: LiveData<LogData>
        get() = _current
    private val _current = MutableLiveData<LogData>()

    val serializedLogs: LiveData<String>
        get() = _serializedLogs
    private val _serializedLogs = MutableLiveData<String>()

    internal fun updateCurrentLog(data: LogData) {
        _current.postValue(data)
    }

    internal fun serializeLogs() {
        viewModelScope.launch {
            val text = StringBuilder()
            text.append("Pluto Log Trace")
            logs.value?.forEach {
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
            _serializedLogs.postValue(text.toString())
        }
    }

    companion object {
        const val MAX_STACK_TRACE_LINES = 15
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"
    }
}
