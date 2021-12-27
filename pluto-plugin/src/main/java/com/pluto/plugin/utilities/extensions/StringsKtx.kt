package com.pluto.plugin.utilities.extensions

import java.util.Locale

fun String.capitalizeText(default: Locale = Locale.getDefault()): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(default) else it.toString() }
