package com.mocklets.pluto.modules.exceptions.ui.holder

import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.mocklets.pluto.BuildConfig
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.asFormattedDate
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.core.ui.spannable.setSpan
import com.mocklets.pluto.databinding.PlutoItemCrashDetailsHeaderBinding
import com.mocklets.pluto.modules.exceptions.ExceptionData
import com.mocklets.pluto.modules.exceptions.anrs.AnrSupervisor.Companion.MAIN_THREAD_RESPONSE_THRESHOLD

internal class CrashItemDetailsHeaderHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_crash_details_header), actionListener) {

    private val binding = PlutoItemCrashDetailsHeaderBinding.bind(itemView)
    private val timestamp = binding.timestamp
    private val stacktrace = binding.stackTrace
    private val reportCrash = binding.reportCrash.crashReportRoot
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
                            " at  ", context.color(R.color.pluto___text_dark_40)
                        )
                    )
                    append(it)
                }
                val extraTrace = item.stackTrace.size - MAX_STACK_TRACE_LINES
                if (extraTrace > 0) {
                    append(
                        fontColor(
                            "\n\t\t\t + $extraTrace more lines", context.color(R.color.pluto___text_dark_40)
                        )
                    )
                }
            }

            if (isPlutoCrash(item.stackTrace.take(MAX_STACK_TRACE_LINES))) {
                reportCrash.visibility = VISIBLE
                reportCrash.setDebounceClickListener {
                    onAction("report_crash")
                }
            } else {
                reportCrash.visibility = GONE
                reportCrash.setDebounceClickListener {}
            }
        }
    }

    private fun handleTitle(item: ExceptionData) {
        if (item.isANRException) {
            message.text =
                context.getString(R.string.pluto___anr_list_message, MAIN_THREAD_RESPONSE_THRESHOLD)
            title.setSpan {
                context.apply {
                    append(
                        fontColor(
                            getString(R.string.pluto___anr_list_title),
                            color(R.color.pluto___text_dark_80)
                        )
                    )
                }
            }
        } else {
            title.setSpan {
                append("${item.file}\t\t")
                append(
                    fontColor("line: ${item.lineNumber}", context.color(R.color.pluto___text_dark_80))
                )
            }
            message.setSpan {
                append("${item.name}\n")
                append(
                    fontColor("${item.message}", context.color(R.color.pluto___text_dark_60))
                )
            }
        }
    }

    private fun isPlutoCrash(trace: List<String>): Boolean {
        trace.forEach {
            if (it.startsWith(BuildConfig.LIBRARY_PACKAGE_NAME)) {
                return true
            }
        }
        return false
    }

    companion object {
        const val MAX_STACK_TRACE_LINES = 20
    }
}
