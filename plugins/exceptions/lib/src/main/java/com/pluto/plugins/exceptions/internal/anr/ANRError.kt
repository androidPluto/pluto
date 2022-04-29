package com.pluto.plugins.exceptions.internal.anr

import android.os.Looper
import java.io.Serializable
import java.util.TreeMap

/**
 * Error thrown by [com.pluto.plugins.exceptions.internal.anr.ANRWatchDog] when an ANR is detected.
 * Contains the stack trace of the frozen UI thread.
 *
 *
 * It is important to notice that, in an ANRError, all the "Caused by" are not really the cause
 * of the exception. Each "Caused by" is the stack trace of a running thread. Note that the main
 * thread always comes first.
 */
internal class ANRError private constructor(
    st: Serial.Thread?,
    /**
     * The minimum duration, in ms, for which the main thread has been blocked. May be more.
     */
    duration: Long
) :
    Error("Application Not Responding for at least $duration ms.", st) {

    @SuppressWarnings("SerialVersionUIDInSerializableClass") // todo remove this
    private class Serial(private val name: String, private val trace: Array<StackTraceElement>) : Serializable {
        inner class Thread constructor(other: Thread?) : Throwable(name, other) {
            override fun fillInStackTrace(): Throwable {
                stackTrace = trace
                return this
            }
        }
    }

    override fun fillInStackTrace(): Throwable {
        stackTrace = arrayOf()
        return this
    }

    companion object {
        private const val serialVersionUID = 1L

        @SuppressWarnings("ComplexCondition", "UnnecessaryParentheses") // todo remove this
        fun create(duration: Long, prefix: String?, logThreadsWithoutStackTrace: Boolean): ANRError {
            val mainThread = Looper.getMainLooper().thread
            val stackTraces: MutableMap<Thread, Array<StackTraceElement>> = TreeMap { lhs, rhs ->
                if (lhs != null && rhs != null) {
                    if (rhs === mainThread) -1 else rhs.name.compareTo(lhs.name)
                } else 0
            }
            for ((key, value) in Thread.getAllStackTraces()) {
                if (key === mainThread || (key.name.startsWith(prefix!!) && (logThreadsWithoutStackTrace || value.isNotEmpty()))) {
                    stackTraces[key] = value
                }
            }

            // Sometimes main is not returned in getAllStackTraces() - ensure that we list it
            if (!stackTraces.containsKey(mainThread)) {
                stackTraces[mainThread] = mainThread.stackTrace
            }
            var tst: Serial.Thread? = null
            for ((key, value) in stackTraces) tst = Serial(
                getThreadTitle(
                    key
                ),
                value
            ).Thread(tst)
            return ANRError(tst, duration)
        }

        fun createForMainOnly(duration: Long): ANRError {
            val mainThread = Looper.getMainLooper().thread
            val mainStackTrace = mainThread.stackTrace
            return ANRError(Serial(getThreadTitle(mainThread), mainStackTrace).Thread(null), duration)
        }

        private fun getThreadTitle(thread: Thread): String {
            return thread.name + " (state = " + thread.state + ")"
        }
    }
}
