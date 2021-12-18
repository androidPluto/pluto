package com.mocklets.pluto.logger.ui.list

import android.view.ViewGroup
import com.mocklets.pluto.logger.LogData
import com.mocklets.pluto.logger.R
import com.mocklets.pluto.logger.databinding.PlutoLoggerListItemBinding
import com.mocklets.pluto.utilities.extensions.asTimeElapsed
import com.mocklets.pluto.utilities.extensions.color
import com.mocklets.pluto.utilities.extensions.dp
import com.mocklets.pluto.utilities.extensions.inflate
import com.mocklets.pluto.utilities.list.DiffAwareAdapter
import com.mocklets.pluto.utilities.list.DiffAwareHolder
import com.mocklets.pluto.utilities.list.ListItem
import com.mocklets.pluto.utilities.setDebounceClickListener
import com.mocklets.pluto.utilities.spannable.setSpan

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
