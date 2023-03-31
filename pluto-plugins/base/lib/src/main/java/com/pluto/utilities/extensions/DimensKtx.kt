package com.pluto.utilities.extensions

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

val Float.dp2px: Float
    get() {
        val scale = Resources.getSystem().displayMetrics.density
        return this * scale + 0.5f
    }

val Float.px2dp: Float
    get() {
        val scale = Resources.getSystem().displayMetrics.density
        return this / scale + 0.5f
    }

val Float.px2sp: Float
    get() {
        val scale = Resources.getSystem().displayMetrics.scaledDensity
        return this / scale
    }

val Int.twoDigit: String
    get() {
        return String.format(Locale.ENGLISH, "%02d", this)
    }
