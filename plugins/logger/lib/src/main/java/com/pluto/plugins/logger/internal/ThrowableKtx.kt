package com.pluto.plugins.logger.internal

import android.content.Context
import androidx.annotation.Keep
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.spannable.createSpan
import com.pluto.plugins.logger.R

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

internal fun Context?.beautifyAttributes(data: Map<String, Any?>): CharSequence? {
    return this?.createSpan {
        data.forEach {
            append("${it.key} : ")
            if (it.value != null) {
                append(fontColor(semiBold("${it.value}"), context.color(R.color.pluto___text_dark_80)))
            } else {
                append(fontColor(light(italic("null")), context.color(R.color.pluto___text_dark_40)))
            }
            append("\n")
        }
    }?.trim()
}
