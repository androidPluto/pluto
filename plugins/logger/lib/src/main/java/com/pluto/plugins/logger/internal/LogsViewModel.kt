package com.pluto.plugins.logger.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pluto.plugin.utilities.extensions.capitalizeText

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
        val text = StringBuilder()
        logs.value?.forEach {
            text.append("${it.level.label.capitalizeText()}/ ${it.tag}: ${it.message}")
            it.tr?.let {
            }
            it.eventAttributes?.let {
            }
            text.append("\n===\n\n")
        }
        _serializedLogs.postValue(text.toString())
    }
}
