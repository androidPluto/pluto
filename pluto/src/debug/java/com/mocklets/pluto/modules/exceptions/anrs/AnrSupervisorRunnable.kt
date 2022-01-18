package com.mocklets.pluto.modules.exceptions.anrs

import android.os.Handler
import android.os.Looper
import com.mocklets.pluto.Pluto
import com.mocklets.pluto.core.DebugLog
import com.mocklets.pluto.core.extensions.wait
import com.mocklets.pluto.modules.exceptions.ANRException
import com.mocklets.pluto.modules.exceptions.ANRListener
import com.mocklets.pluto.modules.exceptions.anrs.AnrSupervisor.Companion.ANR_WATCHER_THREAD_NAME
import com.mocklets.pluto.modules.exceptions.anrs.AnrSupervisor.Companion.ANR_WATCHER_TIMEOUT
import com.mocklets.pluto.modules.exceptions.anrs.AnrSupervisor.Companion.LOGTAG
import com.mocklets.pluto.modules.exceptions.anrs.AnrSupervisor.Companion.MAIN_THREAD_RESPONSE_THRESHOLD

/**
 * A [Runnable] testing the UI thread every 10s until [ ][.stop] is called
 */
internal class AnrSupervisorRunnable : Runnable {
    /**
     * The [Handler] to access the UI threads message queue
     */
    private val mHandler = Handler(Looper.getMainLooper())
    private var anrListener: ANRListener? = null

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
                        anrListener?.onAppNotResponding(e)
                        Pluto.exceptionRepo.saveANR(e)
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

    fun setListener(listener: ANRListener) {
        anrListener = listener
    }
}
