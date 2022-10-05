package com.pluto.plugins.exceptions.internal.extensions

import com.pluto.plugin.utilities.extensions.capitalizeText

fun getPriorityString(priority: Int) =
    when (priority) {
        Thread.MAX_PRIORITY -> "maximum"
        Thread.MIN_PRIORITY -> "minimum"
        else -> "normal"
    }.capitalizeText()
