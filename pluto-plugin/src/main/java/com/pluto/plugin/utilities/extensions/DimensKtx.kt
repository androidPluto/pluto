package com.pluto.plugin.utilities.extensions

import android.content.res.Resources
import android.util.TypedValue
import java.util.Locale

val Float.dp: Float
    get() {
        val displayMetrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)
    }

val Float.sp: Float
    get() {
        val displayMetrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, displayMetrics)
    }

val Int.twoDigit: String
    get() {
        return String.format(Locale.ENGLISH, "%02d", this)
    }
