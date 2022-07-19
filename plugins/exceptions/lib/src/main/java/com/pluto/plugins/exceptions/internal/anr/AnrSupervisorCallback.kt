package com.pluto.plugins.exceptions.internal.anr

import com.pluto.plugins.exceptions.internal.extensions.notifyAll

/**
 * A [Runnable] which calls [.notifyAll] when run.
 */
internal class AnrSupervisorCallback : Runnable {
    /**
     * Returns whether [.run] was called yet
     *
     * @return true if called, false if not
     */
    /**
     * Flag storing whether [.run] was called
     */
    @get:Synchronized
    var isCalled = false
        private set

    @Synchronized
    override fun run() {
        isCalled = true
        this.notifyAll()
    }
}
