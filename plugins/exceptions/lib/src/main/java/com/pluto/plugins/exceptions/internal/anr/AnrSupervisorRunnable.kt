package com.pluto.plugins.exceptions.internal.anr

import android.os.Handler
import android.os.Looper
import com.pluto.plugin.utilities.DebugLog
import com.pluto.plugins.exceptions.ANRException
import com.pluto.plugins.exceptions.UncaughtANRHandler
import com.pluto.plugins.exceptions.internal.ExceptionAllData
import com.pluto.plugins.exceptions.internal.anr.AnrSupervisor.Companion.ANR_WATCHER_THREAD_NAME
import com.pluto.plugins.exceptions.internal.anr.AnrSupervisor.Companion.ANR_WATCHER_TIMEOUT
import com.pluto.plugins.exceptions.internal.anr.AnrSupervisor.Companion.LOGTAG
import com.pluto.plugins.exceptions.internal.anr.AnrSupervisor.Companion.MAIN_THREAD_RESPONSE_THRESHOLD
import com.pluto.plugins.exceptions.internal.asExceptionData
import com.pluto.plugins.exceptions.internal.asThreadData
import com.pluto.plugins.exceptions.internal.extensions.wait
import com.pluto.plugins.exceptions.internal.persistence.ExceptionDBHandler

/**
 * A [Runnable] testing the UI thread every 10s until [ ][.stop] is called
 */
internal class AnrSupervisorRunnable : Runnable {
    /**
     * The [Handler] to access the UI threads message queue
     */
    private val mHandler = Handler(Looper.getMainLooper())
    private var anrHandler: UncaughtANRHandler? = null

    /**
     * The stop flag
     */
    private var mStopped = false
    /**
     * Returns whether the stop is completed
     *
     * @return true if stop is completed, false if not
     */
    /**
     * Flag indicating the stop was performed
     */
    @get:Synchronized
    var isStopped = true
        private set

    override fun run() {
        Thread.currentThread().name = ANR_WATCHER_THREAD_NAME
        isStopped = false

        while (!Thread.interrupted()) {
            try {
                DebugLog.d(LOGTAG, "Check for ANR...")

                // Create new callback
                val callback = AnrSupervisorCallback()

                // Perform test, Handler should run the callback within 1s
                synchronized(callback) {
                    mHandler.post(callback)
                    callback.wait(MAIN_THREAD_RESPONSE_THRESHOLD)

                    // Check if called
                    if (!callback.isCalled) {
                        val e = ANRException(mHandler.looper.thread)
                        anrHandler?.uncaughtANR(mHandler.looper.thread, e)
                        persistException(mHandler.looper.thread, e)
//                        ExceptionDBHandler.persist(e.asExceptionData())
                        // todo save exception to db
                        // Pluto.exceptionRepo.saveANR(e)
                        /** Wait until the thread responds again */
                        callback.wait()
                    } else {
                        DebugLog.d(LOGTAG, "UI Thread responded within 1s")
                    }
                }
                // Check if stopped
                checkStopped()
                /** Sleep for next test */
                Thread.sleep(ANR_WATCHER_TIMEOUT)
            } catch (e: InterruptedException) {
                break
            }
        }

        // Set stop completed flag
        isStopped = true
        DebugLog.d(LOGTAG, "ANR supervision stopped")
    }

    private fun persistException(thread: Thread, exception: ANRException) {
        ExceptionDBHandler.persist(
            timestamp = System.currentTimeMillis(),
            exception = ExceptionAllData(
                thread = thread.asThreadData(),
                exception = exception.asExceptionData()
            )
        )
    }

    @Synchronized
    @Throws(InterruptedException::class)
    private fun checkStopped() {
        if (mStopped) {
            Thread.sleep(MAIN_THREAD_RESPONSE_THRESHOLD)
            if (mStopped) {
                throw InterruptedException()
            }
        }
    }

    /**
     * Stops the check
     */
    @Synchronized
    fun stop() {
        DebugLog.d(LOGTAG, "Stopping...")
        mStopped = true
    }

    /**
     * Stops the check
     */
    @Synchronized
    fun unstop() {
        DebugLog.d(LOGTAG, "Revert stopping...")
        mStopped = false
    }

    fun setListener(handler: UncaughtANRHandler) {
        anrHandler = handler
    }
}
