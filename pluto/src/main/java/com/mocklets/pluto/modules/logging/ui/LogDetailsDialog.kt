package com.mocklets.pluto.modules.logging.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mocklets.pluto.R
import com.mocklets.pluto.core.DeviceInfo
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.sharing.ContentShare
import com.mocklets.pluto.core.sharing.Shareable
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.core.ui.spannable.createSpan
import com.mocklets.pluto.core.ui.spannable.setSpan
import com.mocklets.pluto.databinding.PlutoLayoutLogDetailsBinding
import com.mocklets.pluto.modules.exceptions.asExceptionData
import com.mocklets.pluto.modules.logging.LogData
import com.mocklets.pluto.modules.logging.ui.LogDetailsDialog.Companion.MAX_STACK_TRACE_LINES

internal class LogDetailsDialog(context: Context, data: LogData) :
    BottomSheetDialog(context, R.style.PlutoBottomSheetDialogTheme) {

    private val sheetView: View = context.inflate(R.layout.pluto___layout_log_details)
    private val binding = PlutoLayoutLogDetailsBinding.bind(sheetView)
    private val deviceInfo = DeviceInfo(context)

    init {
        setContentView(sheetView)
        (sheetView.parent as View).background = ColorDrawable(context.color(R.color.pluto___transparent))

        this.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog

            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                isHideable = false
                skipCollapsed = true
                peekHeight = deviceInfo.height
            }

            binding.title.setSpan {
                append("${context.getString(R.string.pluto___log_details)}  ")
                append(italic(fontColor(data.level.label.uppercase(), context.color(data.level.textColor))))
            }

            binding.cta.setDebounceClickListener {
                ContentShare(context).share(Shareable(title = "Share Log details", content = data.toShareText(context)))
            }

            binding.tag.text = data.tag
            binding.filename.setSpan {
                append(fontColor("called from\n", context.color(R.color.pluto___text_dark_40)))
                append(data.stackTraceElement.methodName)
                append(fontColor(" (", context.color(R.color.pluto___text_dark_40)))
                append(data.stackTraceElement.fileName)
                append(fontColor(", line:", context.color(R.color.pluto___text_dark_60)))
                append(fontColor("${data.stackTraceElement.lineNumber}", context.color(R.color.pluto___text_dark_80)))
                append(fontColor(")", context.color(R.color.pluto___text_dark_40)))
            }
            binding.message.text = data.message
            binding.stackTraceContainer.visibility = GONE
            data.tr?.asExceptionData()?.let {
                binding.stackTraceContainer.visibility = VISIBLE
                binding.stackTrace.setSpan {
                    append("${it.name}: ${it.message}")
                    it.stackTrace.take(MAX_STACK_TRACE_LINES).forEach {
                        append("\n\t\t\t")
                        append(fontColor(" at  ", context.color(R.color.pluto___text_dark_40)))
                        append(it)
                    }
                    val extraTrace = it.stackTrace.size - MAX_STACK_TRACE_LINES
                    if (extraTrace > 0) {
                        append(
                            fontColor(
                                "\n\t\t\t + $extraTrace more lines", context.color(R.color.pluto___text_dark_40)
                            )
                        )
                    }
                }
            }

            if (!data.eventAttributes.isNullOrEmpty()) {
                binding.stackTraceContainer.visibility = VISIBLE
                binding.stackTraceTitle.setSpan {
                    append(context.getString(R.string.pluto___event_attributes))
                    append(fontColor(" (${data.eventAttributes.size})", context.color(R.color.pluto___text_dark_40)))
                }
                binding.stackTrace.text = context.beautifyAttributes(data.eventAttributes)
            }
        }
    }

    companion object {
        const val MAX_STACK_TRACE_LINES = 15
    }
}

private fun Context?.beautifyAttributes(data: Map<String, Any?>): CharSequence? {
    return this?.createSpan {
        data.forEach {
            append("${it.key} : ")
            if (it.value != null) {
                append(fontColor(semiBold("${it.value}"), context.color(R.color.pluto___text_dark_80)))
            } else {
                append(fontColor(light(italic("null")), context.color(R.color.pluto___text_dark_40)))
            }
            append("\n")
        }
    }?.trim()
}

private fun LogData.toShareText(context: Context): String {
    val text = StringBuilder()
    text.append("$tag : $message\n")

    tr?.asExceptionData()?.let {
        text.append("\n${it.name}: ${it.message}\n")
        it.stackTrace.take(MAX_STACK_TRACE_LINES).forEach { trace ->
            text.append("\t at $trace\n")
        }
        if (it.stackTrace.size - MAX_STACK_TRACE_LINES > 0) {
            text.append("\t + ${it.stackTrace.size - MAX_STACK_TRACE_LINES} more lines\n\n")
        }
    }

    if (!eventAttributes.isNullOrEmpty()) {
        text.append("\n${context.getString(R.string.pluto___event_attributes).lowercase()} - ")
        eventAttributes.entries.forEach {
            text.append("\n\t ${it.key} : ${it.value}")
        }
    }

    text.append("\n\n-----\nreport powered by Pluto https://pluto.mocklets.com")
    return text.toString()
}
