package com.mocklets.pluto.modules.exceptions.anrs

import com.mocklets.pluto.core.extensions.notifyAll

/**
 * A [Runnable] which calls [.notifyAll] when run.
 */
internal class AnrSupervisorCallback
/**
 * Creates a new instance
 */
    : Runnable {
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
