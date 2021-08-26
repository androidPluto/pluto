package com.mocklets.pluto

import android.util.Log
import androidx.annotation.Keep
import com.mocklets.pluto.modules.logging.Level
import com.mocklets.pluto.modules.logging.LogsRepo

@Suppress("StringLiteralDuplication")
@Keep
class PlutoLog private constructor() {

    companion object {

        @JvmStatic
        fun v(tag: String, message: String?, tr: Throwable? = null) {
            preProcessLog(Level.Verbose, tag, message, tr) { trace ->
                Log.v("$trace | $tag", "$message", tr)
            }
        }

        @JvmStatic
        fun d(tag: String, message: String?, tr: Throwable? = null) {
            preProcessLog(Level.Debug, tag, message, tr) { trace ->
                Log.d("$trace | $tag", "$message", tr)
            }
        }

        @JvmStatic
        fun i(tag: String, message: String?, tr: Throwable? = null) {
            preProcessLog(Level.Info, tag, message, tr) { trace ->
                Log.i("$trace | $tag", "$message", tr)
            }
        }

        @JvmStatic
        fun w(tag: String, message: String?, tr: Throwable? = null) {
            preProcessLog(Level.Warning, tag, message, tr) { trace ->
                Log.w("$trace | $tag", "$message", tr)
            }
        }

        @JvmStatic
        fun e(tag: String, message: String?, tr: Throwable? = null) {
            preProcessLog(Level.Error, tag, message, tr) { trace ->
                Log.e("$trace | $tag", "$message", tr)
            }
        }

        @JvmStatic
        fun event(tag: String, event: String, attributes: HashMap<String, Any?>?) {
            if (!isValidInitialisation()) return
            val stackTrace = Thread.currentThread().stackTraceElement()
            LogsRepo.saveEvent(Level.Event, tag, event, attributes, stackTrace)
            Log.d("${stackTrace.formattedStack()} | $tag", "$event => $attributes")
        }

        private fun preProcessLog(
            level: Level,
            tag: String,
            message: String?,
            tr: Throwable? = null,
            next: ((String) -> Unit)
        ) {
            if (isValidInitialisation()) {
                val stackTrace = Thread.currentThread().stackTraceElement()
                LogsRepo.save(level, tag, message, tr, stackTrace)
                next.invoke(stackTrace.formattedStack())
            }
        }

        private fun isValidInitialisation(): Boolean {
            Pluto.appContext?.let {
                return true
            }
            Log.e("pluto", "PlutoLog not printing as Pluto is not initialised.")
            return false
        }
    }
}

private fun Thread.stackTraceElement(): StackTraceElement {
    stackTrace.forEach {
        if (!it.className.startsWith(BuildConfig.LIBRARY_PACKAGE_NAME) &&
            !it.className.startsWith("java.lang.") &&
            !it.className.startsWith("dalvik.system.")
        ) {
            return it
        }
    }
    return stackTrace[if (name == "main") MAIN_THREAD_INDEX else DAEMON_THREAD_INDEX]
}

private const val MAIN_THREAD_INDEX = 6
private const val DAEMON_THREAD_INDEX = 5

@Keep
private fun StackTraceElement.formattedStack(): String = "$methodName($fileName:$lineNumber)"
