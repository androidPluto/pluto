package com.mocklets.pluto.core.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

fun Long.asFormattedDate(dateFormat: String = "MMM dd, yyyy, HH:mm:ss"): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return formatter.format(calendar.time)
}

fun Long.asTimeElapsed(): String {
    val elapsed = System.currentTimeMillis() - this
    val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsed)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsed)
    val hours = TimeUnit.MILLISECONDS.toHours(elapsed)
    val days = TimeUnit.MILLISECONDS.toDays(elapsed)

    return when {
        seconds.toInt() == 0 -> "just now"
        seconds < SEC_COUNT -> "$seconds secs ago"
        minutes < MIN_COUNT -> "$minutes mins ago"
        hours < HR_COUNT -> "$hours hours ago"
        else -> "$days days ago"
    }
}

private const val SEC_COUNT = 60
private const val MIN_COUNT = 60
private const val HR_COUNT = 24
