package com.pluto.tools.modules.currentScreen

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.pluto.tools.R
import com.pluto.utilities.extensions.color
import com.pluto.utilities.extensions.dp2px
import com.pluto.utilities.spannable.createSpan

internal class CurrentScreenView(context: Context) : AppCompatTextView(context) {

    private var lastInfo: CharSequence? = null

    init {
        setBackgroundColor(context.color(R.color.pluto___dark_80))
        textSize = TEXT_SIZE
        setTextColor(context.color(R.color.pluto___white))
        gravity = Gravity.CENTER
        typeface = ResourcesCompat.getFont(context, R.font.muli_semibold)
        setPadding(10f.dp2px.toInt(), 4f.dp2px.toInt(), 10f.dp2px.toInt(), 4f.dp2px.toInt())
    }

    fun updateText(activity: CharSequence?, fragment: CharSequence?) {
        var value: CharSequence? = if (activity.toString().startsWith(PLUTO_PKG_PREFIX, false)) {
            context.createSpan {
                append(light(italic(fontColor("~ Pluto Screen ~", context.color(R.color.pluto___white_60)))))
            }
        } else {
            context.createSpan {
                append("$fragment ($activity)")
            }
        }
        if (!TextUtils.isEmpty(value)) {
            lastInfo = text
        } else {
            value = lastInfo
        }
        text = value
    }

    companion object {
        private const val TEXT_SIZE = 14f
        private const val PLUTO_PKG_PREFIX = "com.pluto"
    }
}
