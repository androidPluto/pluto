package com.pluto.plugins.network.internal.interceptor.ui.components

import android.content.Context
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.spannable.createSpan
import com.pluto.plugins.network.R

internal fun waitingText(context: Context) = context.createSpan {
    append(
        italic(
            light(
                fontColor(
                    context.getString(R.string.pluto_network___waiting_for_response),
                    context.color(R.color.pluto___text_dark_40)
                )
            )
        )
    )
}

internal fun tapIndicatorText(context: Context) = context.createSpan {
    append("\t\t")
    append(
        italic(
            light(
                fontColor(
                    context.getString(R.string.pluto_network___tap_for_details),
                    context.color(R.color.pluto___text_dark_40)
                )
            )
        )
    )
}

internal fun binaryBodyText(context: Context) = context.createSpan {
    append(
        italic(
            light(
                fontColor(
                    context.getString(R.string.pluto_network___binary_body),
                    context.color(R.color.pluto___text_dark_40)
                )
            )
        )
    )
}
