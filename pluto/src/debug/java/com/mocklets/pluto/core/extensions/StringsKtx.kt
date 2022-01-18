package com.mocklets.pluto.core.extensions

import java.util.*

fun String.capitalizeText(default: Locale = Locale.getDefault()): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(default) else it.toString() }
