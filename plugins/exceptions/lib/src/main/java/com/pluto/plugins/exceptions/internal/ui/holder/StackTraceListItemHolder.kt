package com.pluto.plugins.exceptions.internal.ui.holder

import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.dp
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepItemCrashDetailsThreadStackTraceListBinding
import com.pluto.plugins.exceptions.internal.ProcessThread
import com.pluto.plugins.exceptions.internal.getStateStringSpan

internal class StackTraceListItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_excep___item_crash_details_thread_stack_trace_list), actionListener) {

    private val binding = PlutoExcepItemCrashDetailsThreadStackTraceListBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is ProcessThread) {
            binding.thread.text = item.name
            binding.thread.setCompoundDrawablesWithIntrinsicBounds(
                if (item.name == "main") R.drawable.pluto_excep___ic_main_thread else R.drawable.pluto_excep___ic_non_main_thread,
                0, 0, 0
            )
            binding.thread.compoundDrawablePadding = 8f.dp.toInt()
            binding.threadState.text = getStateStringSpan(context, item.state)
            binding.stackTrace.setSpan {
                item.stackTrace.take(MAX_STACK_TRACE_LINES).forEach {
                    append(fontColor(" at  ", context.color(R.color.pluto___text_dark_40)))
                    append("$it\n")
                }
                val extraTrace = item.stackTrace.size - MAX_STACK_TRACE_LINES
                if (extraTrace > 0) {
                    append(
                        fontColor(
                            "\t + $extraTrace more lines\n", context.color(R.color.pluto___text_dark_40)
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
