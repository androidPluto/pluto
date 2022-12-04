package com.pluto.plugins.exceptions

@SuppressWarnings("UnusedPrivateMember", "EmptyFunctionBlock")
object PlutoExceptions {

    /**
     * The threshold for main thread response
     * time before resulting in ANR.
     */
    var mainThreadResponseThreshold = 0

    fun setExceptionHandler(uncaughtExceptionHandler: Thread.UncaughtExceptionHandler) {
    }

    fun setANRHandler(anrHandler: UncaughtANRHandler) {
    }
}
