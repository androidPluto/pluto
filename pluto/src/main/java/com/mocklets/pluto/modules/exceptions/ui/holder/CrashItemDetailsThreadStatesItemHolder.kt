package com.mocklets.pluto.modules.exceptions.ui.holder

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.spannable.setSpan
import com.mocklets.pluto.databinding.PlutoItemAnrThreadStateBinding
import com.mocklets.pluto.modules.exceptions.ProcessThread

internal class CrashItemDetailsThreadStatesItemHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_anr_thread_state), actionListener) {

    private val binding = PlutoItemAnrThreadStateBinding.bind(itemView)

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
