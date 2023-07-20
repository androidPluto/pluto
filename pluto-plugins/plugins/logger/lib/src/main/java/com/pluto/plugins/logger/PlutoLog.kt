package com.pluto.plugins.logger

import android.util.Log
import androidx.annotation.Keep
import com.pluto.plugins.logger.internal.LogsProcessor.Companion.process
import com.pluto.plugins.logger.internal.LogsProcessor.Companion.processEvent
import com.pluto.plugins.logger.internal.LogsProcessor.Companion.stackTraceElement

@Keep
class PlutoLog private constructor() {

    companion object {

        @JvmStatic
        @JvmOverloads
        fun v(tag: String, message: String, tr: Throwable? = null) {
            process(Log.VERBOSE, tag, message, tr, Thread.currentThread().getStackTraceElement())
        }

        @JvmStatic
        @JvmOverloads
        fun d(tag: String, message: String, tr: Throwable? = null) {
            process(Log.DEBUG, tag, message, tr, Thread.currentThread().getStackTraceElement())
        }

        @JvmStatic
        @JvmOverloads
        fun i(tag: String, message: String, tr: Throwable? = null) {
            process(Log.INFO, tag, message, tr, Thread.currentThread().getStackTraceElement())
        }

        @JvmStatic
        @JvmOverloads
        fun w(tag: String, message: String, tr: Throwable? = null) {
            process(Log.WARN, tag, message, tr, Thread.currentThread().getStackTraceElement())
        }

        @JvmStatic
        @JvmOverloads
        fun e(tag: String, message: String, tr: Throwable? = null) {
            process(Log.ERROR, tag, message, tr, Thread.currentThread().getStackTraceElement())
        }

        @JvmStatic
        @JvmOverloads
        fun wtf(tag: String, message: String, tr: Throwable? = null) {
            process(Log.ASSERT, tag, message, tr, Thread.currentThread().getStackTraceElement())
        }

        @JvmStatic
        @JvmOverloads
        fun event(tag: String, event: String, attributes: HashMap<String, Any?>? = null) {
            processEvent(tag, event, attributes, Thread.currentThread().getStackTraceElement())
        }

        private fun Thread.getStackTraceElement(): StackTraceElement {
            val index = if (name == "main") MAIN_THREAD_INDEX else DAEMON_THREAD_INDEX
            return stackTraceElement(index)
        }

        private const val MAIN_THREAD_INDEX = 6
        private const val DAEMON_THREAD_INDEX = 5
    }
}
