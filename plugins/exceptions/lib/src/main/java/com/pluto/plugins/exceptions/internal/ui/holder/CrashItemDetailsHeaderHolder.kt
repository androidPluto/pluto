package com.pluto.plugins.exceptions.internal.ui.holder

import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.asFormattedDate
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepItemCrashDetailsHeaderBinding
import com.pluto.plugins.exceptions.internal.ExceptionData

internal class CrashItemDetailsHeaderHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_excep___item_crash_details_header), actionListener) {

    private val binding = PlutoExcepItemCrashDetailsHeaderBinding.bind(itemView)
    private val timestamp = binding.timestamp
    private val stacktrace = binding.stackTrace
//    private val reportCrash = binding.reportCrash.crashReportRoot
    private val message = binding.message
    private val title = binding.title

    override fun onBind(item: ListItem) {
        if (item is ExceptionData) {
            handleTitle(item)
            timestamp.text = item.timeStamp.asFormattedDate()

            stacktrace.setSpan {
                append("${item.name}: ${item.message}")
                item.stackTrace.take(MAX_STACK_TRACE_LINES).forEach {
                    append("\n\t\t\t")
                    append(
                        fontColor(
                            " at  ", context.color(com.pluto.plugin.R.color.pluto___text_dark_40)
                        )
                    )
                    append(it)
                }
                val extraTrace = item.stackTrace.size - MAX_STACK_TRACE_LINES
                if (extraTrace > 0) {
                    append(
                        fontColor(
                            "\n\t\t\t + $extraTrace more lines", context.color(com.pluto.plugin.R.color.pluto___text_dark_40)
                        )
                    )
                }
            }

//            if (isPlutoCrash(item.stackTrace.take(MAX_STACK_TRACE_LINES))) {
//                reportCrash.visibility = VISIBLE
//                reportCrash.setDebounceClickListener {
//                    onAction("report_crash")
//                }
//            } else {
//                reportCrash.visibility = GONE
//                reportCrash.setDebounceClickListener {}
//            }
        }
    }

    private fun handleTitle(item: ExceptionData) {
        title.setSpan {
            append("${item.file}\t\t")
            append(
                fontColor("line: ${item.lineNumber}", context.color(com.pluto.plugin.R.color.pluto___text_dark_80))
            )
        }
        message.setSpan {
            append("${item.name}\n")
            append(
                fontColor("${item.message}", context.color(com.pluto.plugin.R.color.pluto___text_dark_60))
            )
        }
    }

//    private fun isPlutoCrash(trace: List<String>): Boolean {
//        trace.forEach {
//            if (it.startsWith(BuildConfig.LIBRARY_PACKAGE_NAME)) {
//                return true
//            }
//        }
//        return false
//    }

    companion object {
        const val MAX_STACK_TRACE_LINES = 20
    }
}
