package com.pluto.logger.internal

import android.util.Log
import androidx.annotation.Keep
import com.pluto.logger.BuildConfig

internal class LogsProcessor private constructor() {

    companion object {

        fun process(priority: Int, tag: String, message: String, tr: Throwable?, stackTrace: StackTraceElement) {
            LogsRepo.save(priority2Level(priority), tag, message, tr, stackTrace)
            consolePrint(priority2Level(priority), tag, message, tr, stackTrace)
        }

        fun processEvent(tag: String, event: String, attr: HashMap<String, Any?>?, stackTrace: StackTraceElement) {
            LogsRepo.saveEvent(Level.Event, tag, event, attr, stackTrace)
            consolePrint(Level.Event, tag, "$event => $attr", null, stackTrace)
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
                else -> Level.Debug
            }
        }

        @SuppressWarnings("StringLiteralDuplication")
        private fun consolePrint(level: Level, tag: String, message: String, tr: Throwable?, trace: StackTraceElement) {
            when (level) {
                is Level.Debug -> Log.v("${trace.formattedStack()} | $tag", message, tr)
                is Level.Error -> Log.e("${trace.formattedStack()} | $tag", message, tr)
                is Level.Info -> Log.i("${trace.formattedStack()} | $tag", message, tr)
                is Level.Warning -> Log.w("${trace.formattedStack()} | $tag", message, tr)
                is Level.Verbose -> Log.v("${trace.formattedStack()} | $tag", message, tr)
                is Level.WTF -> Log.wtf("${trace.formattedStack()} | $tag", message, tr)
                is Level.Event -> Log.d("${trace.formattedStack()} | $tag", message)
            }
        }

        @Keep
        fun StackTraceElement.formattedStack(): String = "$methodName($fileName:$lineNumber)"
    }
}
