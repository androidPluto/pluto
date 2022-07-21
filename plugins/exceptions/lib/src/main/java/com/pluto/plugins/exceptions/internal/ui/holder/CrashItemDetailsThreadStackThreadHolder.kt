package com.pluto.plugins.exceptions.internal.ui.holder

import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepItemCrashDetailsThreadStackTraceBinding
import com.pluto.plugins.exceptions.internal.ThreadStates

internal class CrashItemDetailsThreadStackThreadHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_excep___item_crash_details_thread_stack_trace), actionListener) {

    private val binding = PlutoExcepItemCrashDetailsThreadStackTraceBinding.bind(itemView)

    override fun onBind(item: ListItem) {
        if (item is ThreadStates) {
            binding.label.setSpan {
                append(context.getString(R.string.pluto_excep___thread_stack_traces_label))
                append(fontColor(" (${item.states.size})", context.color(R.color.pluto___text_dark_40)))
            }
            binding.root.setDebounceClickListener {
                onAction("thread_stack_trace")
            }
        }
    }
}
