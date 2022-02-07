package com.pluto.logger

import com.pluto.logger.internal.LogsProcessor
import com.pluto.logger.internal.LogsProcessor.Companion.stackTraceElement
import timber.log.Timber

class PlutoTimberTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        LogsProcessor.process(priority, tag ?: "pluto_timber", messageExtractor(message), t, Thread.currentThread().getStackTraceElement())
    }

    private fun messageExtractor(message: String): String {
        val i = 0
        val length = message.length
        var newline = message.indexOf('\n', i)
        newline = if (newline != -1) newline else length
        val end = newline.coerceAtMost(i + MAX_LOG_LENGTH)
        return message.substring(i, end)
    }

    private fun Thread.getStackTraceElement(): StackTraceElement {
        val index = if (name == "main") MAIN_THREAD_INDEX else DAEMON_THREAD_INDEX
        return stackTraceElement(index)
    }

    companion object {
        private const val MAX_LOG_LENGTH = 4000
        private const val MAIN_THREAD_INDEX = 9
        private const val DAEMON_THREAD_INDEX = 8
    }
}
