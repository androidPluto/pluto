package com.mocklets.pluto.modules.exceptions.anrs

import com.mocklets.pluto.modules.exceptions.ANRListener
import java.util.concurrent.Executors

/**
 * A class supervising the UI thread for ANR errors. Use
 * [.start] and [.stop] to control
 * when the UI thread is supervised
 */
internal class AnrSupervisor {
    private val mExecutor = Executors.newSingleThreadExecutor()

    /**
     * The [AnrSupervisorRunnable] running on a separate thread
     */
    private val mSupervisor = AnrSupervisorRunnable()

    /**
     * Starts the supervision
     */
    @Synchronized
    fun start() {
        synchronized(mSupervisor) {
            if (mSupervisor.isStopped) {
                mExecutor.execute(mSupervisor)
            } else {
                mSupervisor.unstop()
            }
        }
    }

    /**
     * Stops the supervision. The stop is delayed, so if
     * start() is called right after stop(),
     * both methods will have no effect. There will be at least one
     * more ANR check before the supervision is stopped.
     */
    @Synchronized
    fun stop() {
        mSupervisor.stop()
    }

    fun setListener(listener: ANRListener) {
        mSupervisor.setListener(listener)
    }

    companion object {
        const val LOGTAG = "Pluto-ANR-watcher"
        const val ANR_WATCHER_THREAD_NAME = "pluto-anr-watcher"
        const val ANR_WATCHER_TIMEOUT: Long = 10_000
        const val MAIN_THREAD_RESPONSE_THRESHOLD: Long = 2_000
    }
}
