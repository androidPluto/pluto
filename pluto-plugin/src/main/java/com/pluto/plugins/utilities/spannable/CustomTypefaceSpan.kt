package com.pluto.plugins.utilities.spannable

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

internal class CustomTypefaceSpan(private val font: Typeface) : MetricAffectingSpan() {
    override fun updateMeasureState(p: TextPaint) {
        update(p)
    }

    override fun updateDrawState(tp: TextPaint) {
        update(tp)
    }

    private fun update(textPaint: TextPaint) {
        textPaint.apply {
            val oldTypeface = typeface
            val oldStyle = oldTypeface?.style ?: 0

            val newTypeface = Typeface.create(font, oldStyle)

            typeface = newTypeface
        }
    }
}
