package com.mocklets.pluto.modules.exceptions

import androidx.annotation.Keep
import com.mocklets.pluto.BuildConfig
import com.mocklets.pluto.core.DeviceFingerPrint
import com.mocklets.pluto.core.ui.list.ListItem

@Keep
internal data class ExceptionAllData(
    val thread: ThreadData? = null,
    val exception: ExceptionData,
    val device: DeviceFingerPrint,
    val threadStateList: ThreadStates? = null
)

@Keep
internal data class ThreadData(
    val id: Long,
    val name: String,
    val priority: Int,
    val isDaemon: Boolean,
    val state: String,
    val group: ThreadGroupData?
) : ListItem()

@Keep
internal data class ThreadGroupData(
    val name: String,
    val parent: String,
    val activeCount: Int
)

@Keep
internal data class ExceptionData(
    val message: String?,
    val name: String?,
    val file: String?,
    val lineNumber: Int,
    val stackTrace: ArrayList<String>,
    val timeStamp: Long = System.currentTimeMillis(),
    val isANRException: Boolean = false
) : ListItem()

@Keep
internal data class ThreadStates(
    val states: List<ProcessThread>
) : ListItem()

@Keep
internal data class ProcessThread(
    val name: String,
    val state: String,
    val stackTrace: ArrayList<String>
) : ListItem()

internal fun Throwable.asExceptionData(isANR: Boolean = false): ExceptionData {
    return ExceptionData(
        name = this.toString().replace(": $message", "", true),
        message = message,
        stackTrace = stackTrace.asStringArray(),
        file = stackTrace.getOrNull(0)?.fileName,
        lineNumber = stackTrace.getOrNull(0)?.lineNumber ?: Int.MIN_VALUE,
        isANRException = isANR
    )
}

internal fun Array<StackTraceElement>.asStringArray(): ArrayList<String> {
    val array = arrayListOf<String>()
    forEach {
        if (it.isNativeMethod) {
            array.add("${it.className}.${it.methodName}(Native Method)")
        } else {
            array.add("${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})")
        }
    }
    return array
}

internal fun Thread.asThreadData(): ThreadData {
    return ThreadData(
        id = id,
        name = name,
        isDaemon = isDaemon,
        state = state.name,
        group = threadGroup.convert(),
        priority = priority
    )
}

private fun ThreadGroup?.convert(): ThreadGroupData? {
    this?.let {
        ThreadGroupData(
            name = it.name,
            parent = it.parent.name,
            activeCount = it.activeCount()
        )
    }
    return null
}

@Keep
data class ReportData(
    val message: String?,
    val name: String?,
    val stackTrace: ArrayList<String>,
    val client: String?,
    val gitSha: String = BuildConfig.GIT_SHA,
    val version: String = BuildConfig.VERSION_NAME,
    val buildType: String = BuildConfig.BUILD_TYPE
)
