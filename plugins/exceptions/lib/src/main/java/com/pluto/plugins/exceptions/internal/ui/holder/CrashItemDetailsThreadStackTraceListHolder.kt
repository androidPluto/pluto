package com.pluto.plugins.exceptions.internal.ui.holder

import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepItemCrashDetailsThreadStackTraceListBinding
import com.pluto.plugins.exceptions.internal.ProcessThread

internal class CrashItemDetailsThreadStackTraceListHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_excep___item_crash_details_thread_stack_trace_list), actionListener) {

    private val binding = PlutoExcepItemCrashDetailsThreadStackTraceListBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is ProcessThread) {
            binding.stackTrace.setSpan {
                append(
                    fontColor(
                        semiBold("${item.name} (${item.state.uppercase()})"),
                        context.color(if (item.state == Thread.State.BLOCKED.name) R.color.pluto___red else R.color.pluto___text_dark_80)
                    )
                )
                item.stackTrace.take(MAX_STACK_TRACE_LINES).forEach {
                    append("\n\t")
                    append(fontColor(" at  ", context.color(R.color.pluto___text_dark_40)))
                    append(it)
                }
                val extraTrace = item.stackTrace.size - MAX_STACK_TRACE_LINES
                if (extraTrace > 0) {
                    append(
                        fontColor(
                            "\n\t + $extraTrace more lines", context.color(R.color.pluto___text_dark_40)
                        )
                    )
                }
            }
        }
    }

    companion object {
        const val MAX_STACK_TRACE_LINES = 10
    }
}
