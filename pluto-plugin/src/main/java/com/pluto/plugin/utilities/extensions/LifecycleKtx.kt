package com.pluto.plugin.utilities.extensions

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun LifecycleCoroutineScope.delayedLaunchWhenResumed(delay: Long, block: suspend CoroutineScope.() -> Unit): Job =
    launch {
        delay(delay)
        launchWhenResumed(block)
    }
