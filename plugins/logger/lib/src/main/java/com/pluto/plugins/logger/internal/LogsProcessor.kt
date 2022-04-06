package com.pluto.plugins.logger.internal

import android.util.Log
import androidx.annotation.Keep
import com.pluto.plugins.logger.BuildConfig
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

internal class LogsProcessor private constructor() {

    companion object {

        fun process(priority: Int, tag: String, message: String, tr: Throwable?, stackTrace: StackTraceElement) {
            LogsRepo.save(priority2Level(priority), tag, message, tr, stackTrace)
            consolePrint(priority2Level(priority), tag, message, tr, stackTrace)
        }

        fun processEvent(tag: String, event: String, attr: HashMap<String, Any?>?, stackTrace: StackTraceElement) {
            val moshi = Moshi.Builder().build()
            val moshiAdapter: JsonAdapter<Map<String, Any?>?> = moshi.adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))
            LogsRepo.saveEvent(Level.Event, tag, event, attr, stackTrace)
            consolePrint(Level.Event, tag, "$event => ${moshiAdapter.toJson(attr)}", null, stackTrace)
        }

        @SuppressWarnings("ComplexCondition")
        fun Thread.stackTraceElement(index: Int): StackTraceElement {
            stackTrace.forEach {
                if (!it.className.startsWith(BuildConfig.LIBRARY_PACKAGE_NAME) &&
                    !it.className.startsWith("java.lang.") &&
                    !it.className.startsWith("dalvik.system.") &&
                    !it.fileName.startsWith("Timber.kt")
                ) {
                    return it
                }
            }
            return stackTrace[index]
        }

        private fun priority2Level(priority: Int): Level {
            return when (priority) {
                Log.DEBUG -> Level.Debug
                Log.ERROR -> Level.Error
                Log.INFO -> Level.Info
                Log.VERBOSE -> Level.Verbose
                Log.WARN -> Level.Warning
                Log.ASSERT -> Level.WTF
                LOG_EVENT_PRIORITY -> Level.Event
                else -> Level.Debug
            }
        }

        private fun consolePrint(level: Level, tag: String, message: String, tr: Throwable?, trace: StackTraceElement) {
            val logTag = "${trace.formattedStack()} | $tag"
            when (level) {
                is Level.Debug -> Log.v(logTag, message, tr)
                is Level.Error -> Log.e(logTag, message, tr)
                is Level.Info -> Log.i(logTag, message, tr)
                is Level.Warning -> Log.w(logTag, message, tr)
                is Level.Verbose -> Log.v(logTag, message, tr)
                is Level.WTF -> Log.wtf(logTag, message, tr)
                is Level.Event -> Log.d(logTag, message)
            }
        }

        @Keep
        fun StackTraceElement.formattedStack(): String = "$methodName($fileName:$lineNumber)"

        const val LOG_EVENT_PRIORITY = 101
    }
}
