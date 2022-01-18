package com.mocklets.pluto.modules.logging.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mocklets.pluto.modules.logging.LogData
import com.mocklets.pluto.modules.logging.LogsRepo

internal class LogsViewModel : ViewModel() {

    val logs: LiveData<List<LogData>>
        get() = LogsRepo.getLogsLD()
}
