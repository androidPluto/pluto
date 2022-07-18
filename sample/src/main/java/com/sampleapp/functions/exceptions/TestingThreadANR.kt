package com.sampleapp.functions.exceptions

import android.os.Handler
import com.pluto.plugins.logger.PlutoLog

class TestingThreadANR : Thread() {

    init {
        name = "TestingThreadANR"
    }

    override fun run() {
        synchronized(mutex) { while (true) testSleep() }
    }

    companion object {
        private const val SLEEP_DURATION = 8 * 1000L
        private const val DEADLOCK_DURATION = 1000L
        val mutex = Any()
        fun testSleep() {
            try {
                sleep(SLEEP_DURATION)
            } catch (e: InterruptedException) {
                PlutoLog.e("ANR-Sleep", "interrupted while sleep", e)
            }
        }

        fun testInfiniteLoop() {
            var i = 0
            while (true) {
                i++
            }
        }

        fun testDeadLock() {
            Handler().postDelayed(
                {
                    synchronized(mutex) {
                        PlutoLog.e("ANR-Failed", "There should be a dead lock before this message")
                    }
                },
                DEADLOCK_DURATION
            )
            TestingThreadANR().start()
        }
    }
}
