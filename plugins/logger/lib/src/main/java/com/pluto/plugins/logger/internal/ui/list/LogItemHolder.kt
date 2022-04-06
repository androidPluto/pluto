package com.pluto.plugins.logger.internal.ui.list

import android.view.ViewGroup
import com.pluto.plugin.utilities.extensions.asTimeElapsed
import com.pluto.plugin.utilities.extensions.color
import com.pluto.plugin.utilities.extensions.dp
import com.pluto.plugin.utilities.extensions.inflate
import com.pluto.plugin.utilities.list.DiffAwareAdapter
import com.pluto.plugin.utilities.list.DiffAwareHolder
import com.pluto.plugin.utilities.list.ListItem
import com.pluto.plugin.utilities.setDebounceClickListener
import com.pluto.plugin.utilities.spannable.setSpan
import com.pluto.plugins.logger.R
import com.pluto.plugins.logger.databinding.PlutoLoggerListItemBinding
import com.pluto.plugins.logger.internal.LogData

internal class LogItemHolder(parent: ViewGroup, actionListener: DiffAwareAdapter.OnActionListener) :
    DiffAwareHolder(parent.inflate(R.layout.pluto_logger___list_item), actionListener) {

    private val binding = PlutoLoggerListItemBinding.bind(itemView)
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
