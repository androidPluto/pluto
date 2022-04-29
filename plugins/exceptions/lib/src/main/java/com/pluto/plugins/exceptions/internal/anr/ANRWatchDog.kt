package com.pluto.plugins.exceptions.internal.anr

import android.os.Debug
import android.os.Handler
import android.os.Looper
import com.pluto.plugin.utilities.DebugLog

/**
 * A watchdog timer thread that detects when the UI thread has frozen.
 */
internal class ANRWatchDog @JvmOverloads constructor(private val timeoutInterval: Int = DEFAULT_ANR_TIMEOUT) : Thread() {
    interface ANRListener {
        /**
         * Called when an ANR is detected.
         *
         * @param error The error describing the ANR.
         */
        fun onAppNotResponding(error: ANRError)
    }

    interface ANRInterceptor {
        /**
         * Called when main thread has froze more time than defined by the timeout.
         *
         * @param duration The minimum time (in ms) the main thread has been frozen (may be more).
         * @return 0 or negative if the ANR should be reported immediately. A positive number of ms to postpone the reporting.
         */
        fun intercept(duration: Long): Long
    }

    interface InterruptionListener {
        fun onInterrupted(exception: InterruptedException)
    }

    private var _anrListener = DEFAULT_ANR_LISTENER
    private var _anrInterceptor = DEFAULT_ANR_INTERCEPTOR
    private var _interruptionListener = DEFAULT_INTERRUPTION_LISTENER
    private val _uiHandler = Handler(Looper.getMainLooper())
    private var _namePrefix: String? = ""
    private var _logThreadsWithoutStackTrace = false
    private var _ignoreDebugger = false

    @Volatile
    private var _tick: Long = 0

    @Volatile
    private var _reported = false
    private val _ticker = Runnable {
        _tick = 0
        _reported = false
    }

    /**
     * Sets an interface for when an ANR is detected.
     * If not set, the default behavior is to throw an error and crash the application.
     *
     * @param listener The new listener or null
     * @return itself for chaining.
     */
    fun setANRListener(listener: ANRListener?): ANRWatchDog {
        _anrListener = listener ?: DEFAULT_ANR_LISTENER
        return this
    }

    /**
     * Sets an interface to intercept ANRs before they are reported.
     * If set, you can define if, given the current duration of the detected ANR and external context, it is necessary to report the ANR.
     *
     * @param interceptor The new interceptor or null
     * @return itself for chaining.
     */
    fun setANRInterceptor(interceptor: ANRInterceptor?): ANRWatchDog {
        _anrInterceptor = interceptor ?: DEFAULT_ANR_INTERCEPTOR
        return this
    }

    /**
     * Sets an interface for when the watchdog thread is interrupted.
     * If not set, the default behavior is to just log the interruption message.
     *
     * @param listener The new listener or null.
     * @return itself for chaining.
     */
    fun setInterruptionListener(listener: InterruptionListener?): ANRWatchDog {
        _interruptionListener = listener ?: DEFAULT_INTERRUPTION_LISTENER
        return this
    }

    /**
     * Set the prefix that a thread's name must have for the thread to be reported.
     * Note that the main thread is always reported.
     * Default "".
     *
     * @param prefix The thread name's prefix for a thread to be reported.
     * @return itself for chaining.
     */
    fun setReportThreadNamePrefix(prefix: String?): ANRWatchDog {
        _namePrefix = prefix ?: ""
        return this
    }

    /**
     * Set that only the main thread will be reported.
     *
     * @return itself for chaining.
     */
    fun setReportMainThreadOnly(): ANRWatchDog {
        _namePrefix = null
        return this
    }

    /**
     * Set that all threads will be reported (default behaviour).
     *
     * @return itself for chaining.
     */
    fun setReportAllThreads(): ANRWatchDog {
        _namePrefix = ""
        return this
    }

    /**
     * Set that all running threads will be reported,
     * even those from which no stack trace could be extracted.
     * Default false.
     *
     * @param logThreadsWithoutStackTrace Whether or not all running threads should be reported
     * @return itself for chaining.
     */
    fun setLogThreadsWithoutStackTrace(logThreadsWithoutStackTrace: Boolean): ANRWatchDog {
        _logThreadsWithoutStackTrace = logThreadsWithoutStackTrace
        return this
    }

    /**
     * Set whether to ignore the debugger when detecting ANRs.
     * When ignoring the debugger, ANRWatchdog will detect ANRs even if the debugger is connected.
     * By default, it does not, to avoid interpreting debugging pauses as ANRs.
     * Default false.
     *
     * @param ignoreDebugger Whether to ignore the debugger.
     * @return itself for chaining.
     */
    fun setIgnoreDebugger(ignoreDebugger: Boolean): ANRWatchDog {
        _ignoreDebugger = ignoreDebugger
        return this
    }

    @SuppressWarnings("LoopWithTooManyJumpStatements")
    override fun run() {
        name = "|ANR-WatchDog|"
        var interval = timeoutInterval.toLong()
        while (!isInterrupted) {
            val needPost = _tick == 0L
            _tick += interval
            if (needPost) {
                _uiHandler.post(_ticker)
            }
            try {
                sleep(interval)
            } catch (e: InterruptedException) {
                _interruptionListener.onInterrupted(e)
                return
            }

            // If the main thread has not handled _ticker, it is blocked. ANR.
            if (_tick != 0L && !_reported) {
                if (!_ignoreDebugger && (Debug.isDebuggerConnected() || Debug.waitingForDebugger())) {
                    DebugLog.w(
                        "ANRWatchdog",
                        "An ANR was detected but ignored because the debugger is connected (you can prevent this with setIgnoreDebugger(true))"
                    )
                    _reported = true
                    continue
                }
                interval = _anrInterceptor.intercept(_tick)
                if (interval > 0) {
                    continue
                }
                val error: ANRError = if (_namePrefix != null) {
                    ANRError.create(_tick, _namePrefix, _logThreadsWithoutStackTrace)
                } else {
                    ANRError.createForMainOnly(_tick)
                }
                _anrListener.onAppNotResponding(error)
                interval = timeoutInterval.toLong()
                _reported = true
            }
        }
    }

    companion object {
        private const val DEFAULT_ANR_TIMEOUT = 5000
        private val DEFAULT_ANR_LISTENER: ANRListener = object : ANRListener {
            override fun onAppNotResponding(error: ANRError) {
                throw error
            }
        }
        private val DEFAULT_ANR_INTERCEPTOR: ANRInterceptor = object : ANRInterceptor {
            override fun intercept(duration: Long): Long {
                return 0
            }
        }
        private val DEFAULT_INTERRUPTION_LISTENER: InterruptionListener = object : InterruptionListener {
            override fun onInterrupted(exception: InterruptedException) {
                DebugLog.w("ANRWatchdog", "Interrupted: " + exception.message)
            }
        }
    }
}
