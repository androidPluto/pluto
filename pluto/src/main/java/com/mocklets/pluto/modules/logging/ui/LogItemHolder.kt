package com.mocklets.pluto.modules.logging.ui

import android.view.ViewGroup
import com.mocklets.pluto.R
import com.mocklets.pluto.core.extensions.asTimeElapsed
import com.mocklets.pluto.core.extensions.color
import com.mocklets.pluto.core.extensions.dp
import com.mocklets.pluto.core.extensions.inflate
import com.mocklets.pluto.core.ui.list.DiffAwareAdapter
import com.mocklets.pluto.core.ui.list.DiffAwareHolder
import com.mocklets.pluto.core.ui.list.ListItem
import com.mocklets.pluto.core.ui.setDebounceClickListener
import com.mocklets.pluto.core.ui.spannable.setSpan
import com.mocklets.pluto.databinding.PlutoItemLogBinding
import com.mocklets.pluto.modules.logging.LogData

internal class LogItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto___item_log), actionListener) {

    private val binding = PlutoItemLogBinding.bind(itemView)
    private val logTag = binding.logtag
    private val message = binding.message
    private val timestamp = binding.timestamp

    override fun onBind(item: ListItem) {
        if (item is LogData) {
            logTag.setSpan {
                append(fontColor(semiBold(item.tag.trim()), context.color(R.color.pluto___text_dark_40)))
                append(
                    fontColor(
                        " | ${item.stackTraceElement.fileName}:${item.stackTraceElement.lineNumber}",
                        context.color(R.color.pluto___text_dark_40)
                    )
                )
            }
            logTag.setCompoundDrawablesWithIntrinsicBounds(item.level.iconRes, 0, 0, 0)
            logTag.compoundDrawablePadding = DRAWABLE_PADDING
            message.setSpan {
                append(semiBold(item.message.trim()))
                item.eventAttributes?.let {
                    append(
                        regular(
                            fontColor(" (${it.size} attributes)", context.color(R.color.pluto___text_dark_60))
                        )
                    )
                }
            }
            timestamp.text = item.timeStamp.asTimeElapsed()
            itemView.setBackgroundColor(itemView.context.color(item.level.color))
            itemView.setDebounceClickListener { onAction("click") }
        }
    }

    private companion object {
        val DRAWABLE_PADDING = 4f.dp.toInt()
    }
}
