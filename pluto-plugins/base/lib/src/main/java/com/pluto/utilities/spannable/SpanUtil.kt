package com.pluto.utilities.spannable

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import com.pluto.plugin.R
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.font
import java.text.Normalizer

inline fun TextView.setSpan(
    bufferType: TextView.BufferType? = null,
    spanBuilder: Builder.() -> Unit
) {
    val builder = Builder(context)
    builder.spanBuilder()
    if (bufferType != null) {
        setText(builder.build(), bufferType)
    } else {
        text = builder.build()
    }
}

inline fun Context.createSpan(spanBuilder: Builder.() -> Unit): CharSequence {
    val builder = Builder(this)
    builder.spanBuilder()
    return builder.build()
}

class Builder(val context: Context) {
    private val spanBuilder = SpannableStringBuilder()

    fun append(text: String) {
        append(SpannableString(text))
    }

    fun append(span: Spannable) {
        spanBuilder.append(span)
    }

    fun append(span: CharSequence) {
        spanBuilder.append(span)
    }

    fun span(s: CharSequence, o: Any) = when (s) {
        is String -> SpannableString(s).apply {
            setSpan(o, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        is SpannableStringBuilder -> s.apply {
            setSpan(o, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        is SpannableString -> s.apply {
            setSpan(o, 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        else -> throw IllegalArgumentException("unhandled type $o")
    }

    fun bold(span: CharSequence): CharSequence {
        return span(span, getFontSpan(R.font.muli_bold))
    }

    fun light(span: CharSequence): CharSequence {
        return span(span, getFontSpan(R.font.muli_light))
    }

    fun regular(span: CharSequence): CharSequence {
        return span(span, getFontSpan(R.font.muli))
    }

    fun semiBold(span: CharSequence): CharSequence {
        return span(span, getFontSpan(R.font.muli_semibold))
    }

    private fun getFontSpan(font: Int) = CustomTypefaceSpan(context.font(font)!!)

    fun fontSize(span: CharSequence, sizeInSp: Int): CharSequence {
        return span(span, AbsoluteSizeSpan(sizeInSp, true))
    }

    fun fontColor(span: CharSequence, color: Int): CharSequence {
        return span(span, ForegroundColorSpan(color))
    }

    fun highlight(span: CharSequence, search: String?): CharSequence {
        if (search.isNullOrEmpty()) return span
        val normalizedText = Normalizer.normalize(span, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()

        val startIndexes = normalizedText.allOccurrences(search)
        if (startIndexes.isNotEmpty()) {
            val highlighted: Spannable = SpannableString(span)
            startIndexes.forEach {
                highlighted.setSpan(
                    BackgroundColorSpan(context.color(R.color.pluto___text_highlight)),
                    it,
                    it + search.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            return highlighted
        }
        return span
    }

    fun clickable(span: CharSequence, listener: ClickableSpan): CharSequence {
        return span(span, listener)
    }

    fun underline(span: CharSequence): CharSequence {
        return span(span, UnderlineSpan())
    }

    fun italic(span: CharSequence): CharSequence {
        return span(span, StyleSpan(Typeface.ITALIC))
    }

    fun build(): CharSequence {
        return spanBuilder
    }
}

private fun String.allOccurrences(search: String): ArrayList<Int> {
    val indexes = arrayListOf<Int>()
    var idx = 0
    while (indexOf(search, idx, true).also { idx = it } >= 0) {
        indexes.add(idx)
        idx++
    }
    return indexes
}
