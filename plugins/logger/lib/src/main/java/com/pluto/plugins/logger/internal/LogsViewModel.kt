package com.pluto.plugins.logger.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

internal class LogsViewModel : ViewModel() {
    val logs: LiveData<List<LogData>>
        get() = LogsRepo.logs

    val current: LiveData<LogData>
        get() = _current
    private val _current = MutableLiveData<LogData>()

    internal fun updateCurrentLog(data: LogData) {
        _current.postValue(data)
    }
}
