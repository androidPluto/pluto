package com.pluto.plugins.exceptions.internal

import android.content.Context
import androidx.annotation.Keep
import com.pluto.plugins.exceptions.BuildConfig
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.internal.extensions.getPriorityString
import com.pluto.utilities.device.Device
import com.pluto.utilities.extensions.color
import com.pluto.utilities.list.ListItem
import com.pluto.utilities.spannable.createSpan
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
internal data class ExceptionAllData(
    val thread: ThreadData? = null,
    val exception: ExceptionData,
    val threadStateList: ThreadStates? = null
)

@Keep
@JsonClass(generateAdapter = true)
internal data class ThreadData(
    val id: Long,
    val name: String,
    val priority: Int,
    val isDaemon: Boolean,
    val state: String,
    val group: ThreadGroupData?
) : ListItem() {
    val priorityString: String = getPriorityString(priority)
}

@Keep
@JsonClass(generateAdapter = true)
internal data class ThreadGroupData(
    val name: String,
    val parent: String,
    val activeCount: Int
)

@Keep
@JsonClass(generateAdapter = true)
internal data class ExceptionData(
    val message: String?,
    val name: String?,
    val file: String?,
    val lineNumber: Int,
    val stackTrace: List<String>,
    val timeStamp: Long = System.currentTimeMillis(),
    val isANRException: Boolean = false
) : ListItem()

@Keep
@JsonClass(generateAdapter = true)
internal data class ThreadStates(
    val states: List<ProcessThread>
) : ListItem()

@Keep
@JsonClass(generateAdapter = true)
internal data class ProcessThread(
    val name: String,
    val state: String,
    val stackTrace: List<String>
) : ListItem()

@Keep
@JsonClass(generateAdapter = true)
internal data class DeviceInfo(
    val appVersionName: String,
    val appVersionCode: Long,
    val androidOs: String?,
    val androidAPILevel: String?,
    val isRooted: Boolean,
    val buildBrand: String?,
    val buildModel: String?,
    val screenHeightPx: Int,
    val screenWidthPx: Int,
    val screenDensityDpi: Int,
    val screenSizeInch: Double,
    val screenOrientation: String
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

internal fun Device.asDeviceInfo(): DeviceInfo {
    return DeviceInfo(
        appVersionName = app.version.name,
        appVersionCode = app.version.code,
        androidOs = software.androidOs,
        androidAPILevel = software.androidAPILevel,
        isRooted = software.isRooted,
        buildBrand = build.brand,
        buildModel = build.model,
        screenHeightPx = screen.heightPx,
        screenWidthPx = screen.widthPx,
        screenDensityDpi = screen.density,
        screenSizeInch = screen.sizeInches,
        screenOrientation = screen.orientation
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

internal fun getStateStringSpan(context: Context, state: String): CharSequence {
    return context.createSpan {
        append(
            bold(
                fontColor(
                    state.uppercase(),
                    context.color(
                        when (state) {
                            Thread.State.BLOCKED.name -> R.color.pluto___red_dark
                            Thread.State.WAITING.name -> R.color.pluto___orange
                            else -> R.color.pluto___text_dark_80
                        }
                    )
                )
            )
        )
    }
}

@Keep
@JsonClass(generateAdapter = true)
data class ReportData(
    val message: String?,
    val name: String?,
    val stackTrace: List<String>,
    val client: String?,
    val gitSha: String = BuildConfig.GIT_SHA,
    val version: String = BuildConfig.VERSION_NAME,
    val buildType: String = BuildConfig.BUILD_TYPE
)
