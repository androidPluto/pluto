package com.pluto.plugin.utilities.extensions

import java.util.Locale

fun String.capitalizeText(default: Locale = Locale.getDefault()): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(default) else it.toString() }

fun String.truncateExcess(offset: Int): String {
    return if (length > offset) {
        "${take(offset)}..."
    } else {
        this
    }
}
