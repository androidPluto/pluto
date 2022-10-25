package com.pluto.plugins.exceptions

fun interface UncaughtANRHandler {
    fun uncaughtANR(thread: Thread, exception: ANRException)
}
