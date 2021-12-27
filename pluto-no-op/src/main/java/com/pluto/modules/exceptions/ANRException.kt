package com.pluto.modules.exceptions

import androidx.annotation.Keep

@Keep
class ANRException(thread: Thread) : Exception("ANR detected in Pluto-No-Op") {

    val threadStateMap: String

    init {
        stackTrace = thread.stackTrace
        threadStateMap = ""
    }
}
