package com.pluto.plugins.exceptions.internal.ui.holder

import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugins.exceptions.PlutoExceptions
import com.pluto.plugins.exceptions.R
import com.pluto.plugins.exceptions.databinding.PlutoExcepItemCrashDetailsThreadBinding
import com.pluto.plugins.exceptions.internal.ThreadData

internal class CrashItemDetailsThreadHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto_excep___item_crash_details_thread), actionListener) {

    private val binding = PlutoExcepItemCrashDetailsThreadBinding.bind(itemView)
    private val name = binding.name
    private val priority = binding.priority
    private val daemon = binding.daemon
    private val state = binding.state

    override fun onBind(item: ListItem) {
        if (item is ThreadData) {
            name.setSpan {
                append("${item.name.uppercase()}   ")
                append(
                    fontColor("(thread id: ${item.id})", context.color(com.pluto.plugin.R.color.pluto___text_dark_60))
                )
            }
            priority.text = PlutoExceptions.getPriorityString(item.priority)
            daemon.text = item.isDaemon.toString()
            state.text = item.state
        }
    }
}
