package com.pluto.plugins.exceptions.internal.extensions

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
internal fun Any.wait(timeout: Long = 0) = (this as Object).wait(timeout)

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
internal fun Any.notifyAll() = (this as Object).notifyAll()
