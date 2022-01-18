package com.mocklets.pluto.modules.exceptions.ui.holder

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.spannable.setSpan
import com.mocklets.pluto.databinding.PlutoItemCrashDetailsThreadBinding
import com.mocklets.pluto.modules.exceptions.ExceptionRepo
import com.mocklets.pluto.modules.exceptions.ThreadData

internal class CrashItemDetailsThreadHolder(
    parent: ViewGroup,
    actionListener: DiffAwareAdapter.OnActionListener
) : DiffAwareHolder(parent.inflate(R.layout.pluto___item_crash_details_thread), actionListener) {

    private val binding = PlutoItemCrashDetailsThreadBinding.bind(itemView)
    private val name = binding.name
    private val priority = binding.priority
    private val daemon = binding.daemon
    private val state = binding.state

    override fun onBind(item: ListItem) {
        if (item is ThreadData) {
            name.setSpan {
                append("${item.name.uppercase()}   ")
                append(
                    fontColor("(thread id: ${item.id})", context.color(R.color.pluto___text_dark_60))
                )
            }
            priority.text = ExceptionRepo.getPriorityString(item.priority)
            daemon.text = item.isDaemon.toString()
            state.text = item.state
        }
    }
}
